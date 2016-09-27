/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK GIS(tm).
 *
 * Runway SDK GIS(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK GIS(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK GIS(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.gis.geometry;

import java.util.ArrayList;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 * Assists in converting WKT into a GeoPoint and a GeoMultiPolygon, among other things.
 * 
 * @author terraframe
 */
public class GeometryHelper {
	private static double TRIANGLE_DELTA = 0.01d;
	private static double OUTLINE_DELTA = 0.00000001d;
	
	/**
	 * Return a Geometry object created from the given Well Known Text string.
	 * 
	 * @param wktString
	 * @return
	 * @throws ParseException 
	 */
	public Geometry parseGeometry(String wktString) throws ParseException {
		WKTReader r = new WKTReader();
		return r.read(wktString);
	}

	/**
	 * Return a Point object that corresponds to the given Geometry object.
	 * 
	 * For a Point, we'll return the Point, otherwise we'll return the centroid
	 * of the given Geometry
	 * 
	 * @param g
	 * @return
	 */
	public Point getGeoPoint(Geometry g) {
		Point geoPoint = null;

		if (g instanceof Point) {
			geoPoint = (Point) g;
		} else {
			geoPoint = g.getCentroid();
			if (!g.isValid()) {
				geoPoint = g.getFactory().createPoint(g.getCoordinate());
			}
		}

		return geoPoint;
	}

	/**
	 * Return a MultiPolygon object that corresponds to the given Geometry
	 * object.
	 * 
	 * For a point, we'll return a triangle centered around the point. For a
	 * LineString or MultiLineString, we'll return a MultiPolygon created by
	 * tracing the outline(s) of the LineString/MultiLineString. For a
	 * Polygon, we'll return a MultiPolygon that contains only that polygon For
	 * a MultiPolygon, we'll just return it
	 * 
	 * @param g
	 * @return
	 */
	public MultiPolygon getGeoMultiPolygon(Geometry g) {
		MultiPolygon geoMultiPolygon = null;

		if (g instanceof Point) {
			Coordinate[] points = this.createTriangle(g.getCoordinate());
			geoMultiPolygon = new MultiPolygon(new Polygon[] { this.createPolygon(points, g.getFactory()) }, g.getFactory());
		} else if (g instanceof MultiPolygon) {
			geoMultiPolygon = (MultiPolygon) g;
		} else if (g instanceof Polygon) {
			geoMultiPolygon = new MultiPolygon(new Polygon[] { (Polygon) g }, g.getFactory());
		} else if (g instanceof LineString) {
			geoMultiPolygon = new MultiPolygon(new Polygon[] { this.createPolygon((LineString) g) }, g.getFactory());
		} else if (g instanceof MultiLineString) {
			MultiLineString mls = (MultiLineString) g;
			Polygon[] polygons = new Polygon[mls.getNumGeometries()];
			for (int n = 0; n < mls.getNumGeometries(); n++) {
				polygons[n] = this.createPolygon((LineString) mls.getGeometryN(n));
			}
			geoMultiPolygon = new MultiPolygon(polygons, g.getFactory());
		}
		return geoMultiPolygon;
	}

	/**
	 * Create a triangle of points centered around the given coordinate
	 * 
	 * @param center
	 * @return
	 */
	private Coordinate[] createTriangle(Coordinate center) {
		Coordinate[] points = new Coordinate[4];
		points[0] = new Coordinate(center);
		points[0].y += TRIANGLE_DELTA;
		points[1] = new Coordinate(center);
		points[1].y -= TRIANGLE_DELTA;
		points[1].x -= TRIANGLE_DELTA;
		points[2] = new Coordinate(center);
		points[2].y -= TRIANGLE_DELTA;
		points[2].x += TRIANGLE_DELTA;
		points[3] = new Coordinate(points[0]);
		return points;
	}
	
	/**
	 * @param lineString
	 * @return
	 */
	private Polygon createPolygon(LineString lineString) {
		Coordinate[] linePoints = lineString.getCoordinates();
		
		/*		
		 * If there are no points, create an empty polygon
		 * If the LineString is closed (i.e. the start is the same as the end)
		 * then it's basically already a polygon, so just create a polygon from
		 * it
		*/
		if (linePoints.length == 0 || lineString.isClosed()) {
			return this.createPolygon(linePoints, lineString.getFactory());
		}

		/*
		 * If there is only a single point, then we'll create a triangle around
		 * that point, just as we would have if it were a Point.
		 */
		if (linePoints.length == 1) {
			Coordinate[] newPoints = this.createTriangle(linePoints[0]);
			return this.createPolygon(newPoints, lineString.getFactory());
		}
		
		/*
		 * If there are two points, we need to interpolate a point in the middle
		 * in order to be able to generate a "toothpick"
		 */
		if (linePoints.length == 2) {
			Coordinate[] newPoints = new Coordinate[3];
			newPoints[0] = linePoints[0];
			//Interpolate a third, middle position so that we can create a diamond!
			newPoints[1] = new Coordinate((linePoints[0].x + linePoints[1].x) / 2.0d, (linePoints[0].y + linePoints[1].y) / 2.0d);
			newPoints[2] = linePoints[1];
			linePoints = newPoints;
		}
		
		/* Create a new list of coordinates to hold the outline */
		ArrayList<Coordinate> outline = new ArrayList<Coordinate>();
		
		/* Basically, we're going to create a "toothpick."  We'll start at the 
		 * starting point of the line, and then go to the right/above and continue on that
		 * "side" of the line, staying slightly to that side of each point along the line
		 * until we get to the end of the line.  Then we'll trace backwards, mpving along
		 * the opposite side until we get back to the start of the line.
		 */
		
		/* Add the starting point of the line */
		Coordinate offsetPoint = addFirstPoint(outline, linePoints[0], linePoints[1]);
		
		/* Trace up the outside of the line until you get to the end */
		for (int i=1; i < linePoints.length-1; i++) {
			offsetPoint = addBisectedAnglePoint(outline, linePoints[i-1], linePoints[i], linePoints[i+1], offsetPoint);
		}
		
		/* Add the end point of the original line, which is the midpoint of the outline */
		offsetPoint = addMidPoint(outline, linePoints[linePoints.length-1], linePoints[linePoints.length-2], offsetPoint);
		
		/* Trace back down the other side of the line until you get back to the start */
		for (int i=linePoints.length - 2; i > 0; i--) {
			offsetPoint = addBisectedAnglePoint(outline, linePoints[i+1], linePoints[i], linePoints[i-1], offsetPoint);
		}
		
		/* Add the starting point of the line, which is the endpoint of the outline */
		addLastPoint(outline, linePoints[0]);
		
		/* Convert the list to an array, and generate the polygon */
		Coordinate[] full = outline.toArray(new Coordinate[outline.size()]);
		return this.createPolygon(full, lineString.getFactory());
	}

	/**
	 * Return a new polgyon created from the list of points specified
	 * 
	 * @param coordinates
	 * @param factory
	 * @return
	 */
	private Polygon createPolygon(Coordinate[] coordinates, GeometryFactory factory) {
		LinearRing ring = factory.createLinearRing(coordinates);
		Polygon polygon = factory.createPolygon(ring, null);
		return polygon;
	}
	
	/**
	 * Add the first point of the line to the outline, and return a slightly offset
	 * point (to the right or above), which will determine the "side" of the original
	 * line that we'll trace along.
	 * 
	 * @param outline
	 * @param thisPoint
	 * @param nextPoint
	 * @return
	 */
	private Coordinate addFirstPoint(ArrayList<Coordinate> outline, Coordinate thisPoint, Coordinate nextPoint) {
		/* Add this point to the outline */
		outline.add(thisPoint);
		
		/* Find the two points that are OUTLINE_DELTA away from the starting point, and
		 * perpendicular to the line from the starting point to the second point */
		Coordinate[] perpendiculars = this.getPerpendicularPoints(thisPoint, nextPoint);
		
		/* Now order the two points such that the point that is more right (or, if they're both
		 * vertically aligned, more to the top) is first */
		this.orderPerpendiculars(perpendiculars);
		
		/* Return the "more right/up" point, which will then define the side that we're going
		 * to trace on (to start with) */
		return perpendiculars[0];
	}
	
	/**
	 * For every point that is not the start or end, we're going to find the two points that
	 * are slightly offset from that point, and along the line that bisects the angle between them.
	 * We'll select the point that is on the same side of the original line as we are.
	 * 
	 * @param outline
	 * @param lastPoint
	 * @param thisPoint
	 * @param nextPoint
	 * @param offsetPoint The most recently defined point on the current side of the line
	 * @return
	 */
	private Coordinate addBisectedAnglePoint(ArrayList<Coordinate> outline, Coordinate lastPoint, Coordinate thisPoint, Coordinate nextPoint, Coordinate offsetPoint) {
		/* Find the two points that are on the bisecting line */
		Coordinate[] perpendiculars = this.getPerpendicularPoints(outline, lastPoint, thisPoint, nextPoint);
		
		/* Order the two points such that the point on the current side of the line is first */
		this.orderPerpendiculars(perpendiculars, lastPoint, thisPoint, offsetPoint);
	
		/* Add the point that is on our side of the line, and return it for use 
		 * as the updated point on this side of the original line */
		outline.add(perpendiculars[0]);
		return perpendiculars[0];
	}
	
	/**
	 * Add the final point of the original line (which now becomes the midpoint of the outline.
	 * 
	 * @param outline
	 * @param thisPoint The end point of the original line
	 * @param nextPoint The next-to-last point of the original line (which will be the next point since we're about to turn around and trace back down the other side).
	 * @param offsetPoint The most recent point on this (right/up) side of the line
	 * @return
	 */
	private Coordinate addMidPoint(ArrayList<Coordinate> outline, Coordinate thisPoint, Coordinate nextPoint, Coordinate offsetPoint) {
		/* Add the last point of the line to the outline */
		outline.add(thisPoint);
		
		/* Find the two points that are OUTLINE_DELTA away from the end point, and
		 * perpendicular to the line from the end point to the nest-to-last point */
		Coordinate[] perpendiculars = this.getPerpendicularPoints(thisPoint, nextPoint);
		
		/* Sort the two points such that the one that is on "this side" of the line is first */
		this.orderPerpendiculars(perpendiculars, nextPoint, thisPoint, offsetPoint);
		
		/* Now return the OTHER point (to be used to define the "other side" of the line to trace back down */
		return perpendiculars[1];
	}

	/**
	 * Add the starting point of the original line to the outline--which then becomes the last
	 * point of the outline and closes the loop.
	 * 
	 * @param outline
	 * @param p1
	 * @return
	 */
	private Coordinate addLastPoint(ArrayList<Coordinate> outline, Coordinate p1) {
		outline.add(p1);
		return p1;
	}

	/**
	 * Return the pair of points that are OUTLINE_DELTA away from thisPoint, and are located 
	 * along the line that is perpendicular to the line from thisPoint to otherPoint.
	 * 
	 * @param thisPoint
	 * @param otherPoint
	 * @return
	 */
	private Coordinate[] getPerpendicularPoints(Coordinate thisPoint, Coordinate otherPoint) {
		Coordinate[] perpendiculars = new Coordinate[2];
		
		/* Generate the vector from thisPoint to otherPoint, and calculate it's length */
		double dx = otherPoint.x - thisPoint.x;
		double dy = otherPoint.y - thisPoint.y;
		double l = this.length(dx, dy);
		
		/* The normals to the given line segment are (-dy, dx) and (dy, -dx).  We need to divide by the length to generate
		 * unit normals, and then multiply by OFFSET_DELTA to make them the length we want, and add those to thisPoint to get the points.
		 * See: http://stackoverflow.com/questions/1243614/how-do-i-calculate-the-normal-vector-of-a-line-segment */
		perpendiculars[0] = new Coordinate(thisPoint.x + (OUTLINE_DELTA * -dy / l), thisPoint.y + (OUTLINE_DELTA * dx / l));
		perpendiculars[1] = new Coordinate(thisPoint.x + (OUTLINE_DELTA * dy / l), thisPoint.y + (OUTLINE_DELTA * -dx / l));
		
		return perpendiculars;
	}
	
	/**
	 * Return the pair of points that are OUTLINE_DELTA away from thisPoint, and are located along the 
	 * line that bisects the angle between the line segments thisPoint->lastPoint and thisPoint->nextPoint.
	 * 
	 * @param outline
	 * @param lastPoint
	 * @param thisPoint
	 * @param nextPoint
	 * @return
	 */
	private Coordinate[] getPerpendicularPoints(ArrayList<Coordinate> outline, Coordinate lastPoint, Coordinate thisPoint, Coordinate nextPoint) {
		Coordinate[] perpendiculars = new Coordinate[2];

		/* Generate the vectors a (thisPoint->lastPoint) and b (thisPoint->nextPoint) and their lengths */
		Coordinate a = new Coordinate(lastPoint.x-thisPoint.x, lastPoint.y-thisPoint.y);
		Coordinate b = new Coordinate(nextPoint.x-thisPoint.x, nextPoint.y-thisPoint.y);
		double la = this.length(a.x, a.y);
		double lb = this.length(b.x, b.y);
		
		/* The bisecting vector c is |b|a + |a|b 
		 * See: http://www.physicsforums.com/archive/index.php/t-64552.html */
		Coordinate c = new Coordinate((la * b.x) + (lb * a.x), (la * b.y) + (lb * a.y)); 
		double lc = this.length(c.x, c.y);
		
		/* If the length of c is 0, then a and b are co-linear.  In that case, we should just
		 * use the points that are perpendicular from the line from thisPoint to nextPoint. */
		if (lc > 0.0) {
			/* Again, divide by length to get unit vectors, multiply by 
			 * OUTLINE_DELTA to get the distance we want, and add it to thisPoint */
			perpendiculars[0] = new Coordinate(thisPoint.x + (OUTLINE_DELTA * c.x / lc), thisPoint.y + (OUTLINE_DELTA * c.y / lc));
			perpendiculars[1] = new Coordinate(thisPoint.x - (OUTLINE_DELTA * c.x / lc), thisPoint.y - (OUTLINE_DELTA * c.y / lc));
		} else {
			perpendiculars = this.getPerpendicularPoints(thisPoint, nextPoint);
		}

		return perpendiculars;
	}

	/**
	 * Order the perpendicular points such that the point to the left 
	 * or above, if they're vertically aligned) comes first.
	 * 
	 * @param perpendiculars
	 */
	private void orderPerpendiculars(Coordinate[] perpendiculars) {
		boolean isFirstLeftAbove = (perpendiculars[0].x < perpendiculars[1].x || (perpendiculars[0].x == perpendiculars[1].x && perpendiculars[0].y > perpendiculars[1].y));
		
		/* If the current first one is left/down, swap them */
		if (!isFirstLeftAbove) {
			Coordinate temp = perpendiculars[0];
			perpendiculars[0] = perpendiculars[1];
			perpendiculars[1] = temp;
		}
	}

	/**
	 * Order the perpendicular (really, bisecting) points such that the point
	 * that is on the same side of the original line as offsetPoint comes first.
	 * 
	 * @param perpendiculars
	 * @param thisPoint
	 * @param nextPoint
	 * @param offsetPoint
	 */
	private void orderPerpendiculars(Coordinate[] perpendiculars, Coordinate thisPoint, Coordinate nextPoint, Coordinate offsetPoint) {
		/* In order to find the point that is on the same side of the original line as offsetPoint,
		 * we'll check to see if the line segment from offsetPoint to the first perpendicular point intersects (crosses)
		 * the original line segment from thisPoint to nextPoint.  If it does, then it's on the wrong side of the orignial line,
		 * which means that the second point is on the "right" side, so we'll swap them
		 * See: http://www.math.niu.edu/~rusin/known-math/95/line_segs
		 */
		if ( (sgn(thisPoint, nextPoint, offsetPoint) != sgn(thisPoint, nextPoint, perpendiculars[0])) && (sgn(thisPoint, offsetPoint, perpendiculars[0]) != sgn(nextPoint, offsetPoint, perpendiculars[0])) ) {
			Coordinate temp = perpendiculars[0];
			perpendiculars[0] = perpendiculars[1];
			perpendiculars[1] = temp;
		}
	}
	
	/**
	 * Generate the "sign" of the determinant of the given triangle.
	 * 
	 * @param u
	 * @param v
	 * @param w
	 * @return
	 */
	private boolean sgn(Coordinate u, Coordinate v, Coordinate w) {
		double det = ((u.x-w.x) * (v.y-w.y)) - ((u.y-w.y) * (v.x-w.x));
		return det > 0;
	}

	/**
	 * Return the length of the given vector
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private double length(double x, double y) {
		return Math.sqrt((x * x) + (y * y));
	}
}

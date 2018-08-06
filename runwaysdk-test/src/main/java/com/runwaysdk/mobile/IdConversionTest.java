/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.mobile;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.ClientSession;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.dataaccess.DuplicateDataException;
import com.runwaysdk.session.Request;

public class IdConversionTest
{
  private static IdConverter idConverter;

  private static String      customUsernameAndPassword = "Sir LikesToLoginALot";

  private BidiMap            mobileIdToSessionId;

  @Request
  @BeforeClass
  public static void classSetUp()
  {
    try
    {
      UserDAO user = UserDAO.newInstance();
      user.setUsername(customUsernameAndPassword);
      user.setPassword(customUsernameAndPassword);
      user.setSessionLimit(String.valueOf(mobileIds.length));
      user.setInactive(false);
      user.apply();
    }
    catch (DuplicateDataException e)
    {
      System.out.println("DuplicateDataException received, assuming custom test user '" + customUsernameAndPassword + "' already exists.");
    }
  }

  @Request
  @AfterClass
  public static void classTearDown()
  {
  }

  private static final String[] mobileIds = { "eop34mgkvpf3o4gmG$", "Egoepe4kgom", "AWERwevop43jvp", "[e3kopwer4gjfpo34g", "AEO$pgkofergb[", "RTHp[ffrlnbp[rt", "RTht[hep45", "Rthrthk[fdpoergf", "rergopieddrg", "Erff[gko4", "9023IUTR03ffW4FJE", "AW904FJW0OPEfCKF34F", "AW[ER-KPEORFK43", "A3E4[KEfPORFK", "A34P-fFKEF4", "A[EOPf4FKPO34KF43F", "AWE[FK4Pfe3KF", "0PLTweH0YHLYH", "-HLY-fvPLT[", "#$F34gofkefdpogk4", "aefmogp34G$G", "AE4pok4vwep3fk4", "AWEF4egofk4opg", "AErgoepkp4gfok", "REgrtphfkprh", "ERgkecopgkp4", "Egpokefef5gm4g", "ERGmoedf5pgm", "ERGjepgg5gmg" };

  private static final String[] globalIds = { "z6mf0elsj3gpcj9bw59u091vl43pdp4l6z5taiv6z6lz21rwkh0idjv390pd1c1a", "y09vb6ftinuchoiruu3hxmdrwl4w7vyf6z5taiv6z6lz21rwkh0idjv390pd1c1a", "05bpilbn6lueprawrvkbzxbh3o41zj7n6z5taiv6z6lz21rwkh0idjv390pd1c1a", "qxqxdw6esnummho0ri67hw8schpi3qf66z5taiv6z6lz21rwkh0idjv390pd1c1a", "73p5vkmq655suciijth4c8g7i0dgi7ld6z5taiv6z6lz21rwkh0idjv390pd1c1a", "8ctmf1299sv9vujnfx1mamkqo87x9wpn6z5taiv6z6lz21rwkh0idjv390pd1c1a", "xbs28wb11jrg1dxun78u330jct930e286z5taiv6z6lz21rwkh0idjv390pd1c1a", "xddk39nkqyc7jlkzngbirk6uur1z9xxc6z5taiv6z6lz21rwkh0idjv390pd1c1a", "2dukyrbp6urlhdypvnppomeynzy4yvld6z5taiv6z6lz21rwkh0idjv390pd1c1a", "qlxntbg8uij087g07nc4761o8xqmebmv6z5taiv6z6lz21rwkh0idjv390pd1c1a", "0c80qnlail9gsnt0ulqjmzx6lh4ddiqz6z5taiv6z6lz21rwkh0idjv390pd1c1a",
      "282i4u6101aet05w4axc77dv7htol6h06z5taiv6z6lz21rwkh0idjv390pd1c1a", "xazfteinulsa20lbhjaeiy7ilg31rm736z5taiv6z6lz21rwkh0idjv390pd1c1a", "0o6mmxu34ntopjkxp934kqydj9izlgj36z5taiv6z6lz21rwkh0idjv390pd1c1a", "sa5ted63cv5s9f86ztrrg3eqdlcdgqk26z5taiv6z6lz21rwkh0idjv390pd1c1a", "xeeyxo1mh2cfk3evd4kqyl294efxfby86z5taiv6z6lz21rwkh0idjv390pd1c1a", "77is24nu25848w8pwyfngbvhjdatd75g6z5taiv6z6lz21rwkh0idjv390pd1c1a", "qqpmc7448bljrh7pcpd5rw2mnkba6hvo6z5taiv6z6lz21rwkh0idjv390pd1c1a", "8eqle50a9drmhv3zg83f8iwiup3c4k406z5taiv6z6lz21rwkh0idjv390pd1c1a", "0ltbbpnwscmb6w3sfa47keg44yptr0vf6z5taiv6z6lz21rwkh0idjv390pd1c1a", "x9zqrk19c2pkvh0utfgubg6tkv8qbzpo6z5taiv6z6lz21rwkh0idjv390pd1c1a", "08305bnzf6hah9lq8o5t86jpoclunr4n6z5taiv6z6lz21rwkh0idjv390pd1c1a",
      "8csw7omk7w12p0cykv0iok3xs5pbro1m6z5taiv6z6lz21rwkh0idjv390pd1c1a", "xgmip96afifa2nn96ecmse3pesfaughm6z5taiv6z6lz21rwkh0idjv390pd1c1a", "sk0p7u3xvdzjcz0liqkdxhtid3soo7i16z5taiv6z6lz21rwkh0idjv390pd1c1a", "sgo6drc1cl0slk8pga3xns294gqdsnfo6z5taiv6z6lz21rwkh0idjv390pd1c1a", "xmcd4b21vur1mgzde1287pbdocxq0kdu6z5taiv6z6lz21rwkh0idjv390pd1c1a", "xa5rvqx9g6mapu6l1f7929lp2nbewggu6z5taiv6z6lz21rwkh0idjv390pd1c1a", "72qvfvhmgsz87929uyz3hovifsn0utc86z5taiv6z6lz21rwkh0idjv390pd1c1a", "7bnqf8uyon0jcfb05e9ndov27pmiws7q6z5taiv6z6lz21rwkh0idjv390pd1c1a", "xp837h5yrrg2qj5bf7x5jaolhqiiapo56z5taiv6z6lz21rwkh0idjv390pd1c1a", "zk0z4nlclszjbxc06y0li58z7uldmt1m6z5taiv6z6lz21rwkh0idjv390pd1c1a", "8eehm9kccltibnn79u380y34klfj5mx56z5taiv6z6lz21rwkh0idjv390pd1c1a",
      "0b4ce0fikxvftc2y17xv1pz3kc5qvzd26z5taiv6z6lz21rwkh0idjv390pd1c1a", "qzcn040tl9cyoyo34yc16itff4yolm5u6z5taiv6z6lz21rwkh0idjv390pd1c1a", "0n4llb5gp0v86difh35jfapay30k1kit6z5taiv6z6lz21rwkh0idjv390pd1c1a", "qx8x1b746hzd03ddqkeluph56cnhjvqd6z5taiv6z6lz21rwkh0idjv390pd1c1a", "8eos8lbqfqzfw89ueu39rsmqzsporcvt6z5taiv6z6lz21rwkh0idjv390pd1c1a", "0p93cz59fi8j49citso5tzcfirolwl136z5taiv6z6lz21rwkh0idjv390pd1c1a", "qyklnsj14isgu7hrjb5qrdkygc71neye6z5taiv6z6lz21rwkh0idjv390pd1c1a", "r1umul7ej023s5g7id62os2sgdsbr3xi6z5taiv6z6lz21rwkh0idjv390pd1c1a", "711k65p3lvryhywvwj5ff15yz3avankk6z5taiv6z6lz21rwkh0idjv390pd1c1a", "6y991dno2plyp2v7fvtjiceha2onwf6c6z5taiv6z6lz21rwkh0idjv390pd1c1a", "xv5lnvdrwsh1i01r5526bcvqu8c7ka4u6z5taiv6z6lz21rwkh0idjv390pd1c1a",
      "0r54iu1vzrdao3l2ueue3axyuv8w8htm6z5taiv6z6lz21rwkh0idjv390pd1c1a", "xx0j9yo9qam6ru438zv4jxoirhn1v1vz6z5taiv6z6lz21rwkh0idjv390pd1c1a", "st3ekq7m3qvp8f7b241uo57qge8exxwq6z5taiv6z6lz21rwkh0idjv390pd1c1a", "su1uin9h20rd9cp3erbdso8rxrobouz36z5taiv6z6lz21rwkh0idjv390pd1c1a", "0c0mih0d90w74ozcfbwib77uuez1vhu16z5taiv6z6lz21rwkh0idjv390pd1c1a", "z17d69xdp5686fijh7636sd30a5k55aa6z5taiv6z6lz21rwkh0idjv390pd1c1a",

      "z6mf0elsj3gpcj9bw59u341vl43pdp4l6z5taiv6z6lz21rwkh0idjv390pd1c1a", "y09vb6ftinuchoiruu3hxmd2wl4w7vyf6z5taiv6z6lz21rwkh0idjv390pd1c1a", "05bpilbn6luepr233Gkbzxbh3o41zj7n6z5taiv6z6lz21rwkh0idjv390pd1c1a", "qxqxdw6esnummho34G47hw8schpi3qf66z5taiv6z6lz21rwkh0idjv390pd1c1a", "73p5vkmq655suciERTHRT8g7i0dgi7ld6z5taiv6z6lz21rwkh0idjv390pd1c1a", "8ctmf1299sv9vujnAWERGmkqo87x9wpn6z5taiv6z6lz21rwkh0idjv390pd1c1a", "xbs28wb11jrg1dxuE4GGT30jct930e286z5taiv6z6lz21rwkh0idjv390pd1c1a", "xddk39nkqAWRRG45G4545k6uur1z9xxc6z5taiv6z6lz21rwkh0idjv390pd1c1a", "WEFkyrbp6urlhdypvnppomeynzy4yvld6z5taiv6z6lz21rwkh0idjv390pd1c1a", "3Rxntbg8uij087g07nc4761o8xqmebmv6z5taiv6z6lz21rwkh0idjv390pd1c1a", "GR80qnlail9gsnt0ulqjmzx6lh4ddiqz6z5taiv6z6lz21rwkh0idjv390pd1c1a",
      "GH2i4u6101aet05w4axc77dv7htol6h06z5taiv6z6lz21rwkh0idjv390pd1c1a", "ASzfteinulsa20lbhjaeiy7ilg31rm736z5taiv6z6lz21rwkh0idjv390pd1c1a", "436mmxu34ntopjkxp934kqydj9izlgj36z5taiv6z6lz21rwkh0idjv390pd1c1a", "HG5ted63cv5s9f86ztrrg3eqdlcdgqk26z5taiv6z6lz21rwkh0idjv390pd1c1a", "KJeyxo1mh2cfk3evd4kqyl294efxfby86z5taiv6z6lz21rwkh0idjv390pd1c1a", "PFis24nu25848w8pwyfngbvhjdatd75g6z5taiv6z6lz21rwkh0idjv390pd1c1a", "QWpmc7448bljrh7pcpd5rw2mnkba6hvo6z5taiv6z6lz21rwkh0idjv390pd1c1a", "QQqle50a9drmhv3zg83f8iwiup3c4k406z5taiv6z6lz21rwkh0idjv390pd1c1a", "QEtbbpnwscmb6w3sfa47keg44yptr0vf6z5taiv6z6lz21rwkh0idjv390pd1c1a", "QRzqrk19c2pkvh0utfgubg6tkv8qbzpo6z5taiv6z6lz21rwkh0idjv390pd1c1a", "QT305bnzf6hah9lq8o5t86jpoclunr4n6z5taiv6z6lz21rwkh0idjv390pd1c1a",
      "QYsw7omk7w12p0cykv0iok3xs5pbro1m6z5taiv6z6lz21rwkh0idjv390pd1c1a", "QUmip96afifa2nn96ecmse3pesfaughm6z5taiv6z6lz21rwkh0idjv390pd1c1a", "AS0p7u3xvdzjcz0liqkdxhtid3soo7i16z5taiv6z6lz21rwkh0idjv390pd1c1a", "ADo6drc1cl0slk8pga3xns294gqdsnfo6z5taiv6z6lz21rwkh0idjv390pd1c1a", "AFcd4b21vur1mgzde1287pbdocxq0kdu6z5taiv6z6lz21rwkh0idjv390pd1c1a", "AG5rvqx9g6mapu6l1f7929lp2nbewggu6z5taiv6z6lz21rwkh0idjv390pd1c1a", "AHqvfvhmgsz87929uyz3hovifsn0utc86z5taiv6z6lz21rwkh0idjv390pd1c1a", "AJnqf8uyon0jcfb05e9ndov27pmiws7q6z5taiv6z6lz21rwkh0idjv390pd1c1a", "ZX837h5yrrg2qj5bf7x5jaolhqiiapo56z5taiv6z6lz21rwkh0idjv390pd1c1a", "ZC0z4nlclszjbxc06y0li58z7uldmt1m6z5taiv6z6lz21rwkh0idjv390pd1c1a", "ZVehm9kccltibnn79u380y34klfj5mx56z5taiv6z6lz21rwkh0idjv390pd1c1a",
      "ZB4ce0fikxvftc2y17xv1pz3kc5qvzd26z5taiv6z6lz21rwkh0idjv390pd1c1a", "ZNcn040tl9cyoyo34yc16itff4yolm5u6z5taiv6z6lz21rwkh0idjv390pd1c1a", "ZE4llb5gp0v86difh35jfapay30k1kit6z5taiv6z6lz21rwkh0idjv390pd1c1a", "ZM8x1b746hzd03ddqkeluph56cnhjvqd6z5taiv6z6lz21rwkh0idjv390pd1c1a", "UUos8lbqfqzfw89ueu39rsmqzsporcvt6z5taiv6z6lz21rwkh0idjv390pd1c1a", "JY93cz59fi8j49citso5tzcfirolwl136z5taiv6z6lz21rwkh0idjv390pd1c1a", "LJklnsj14isgu7hrjb5qrdkygc71neye6z5taiv6z6lz21rwkh0idjv390pd1c1a", "PFumul7ej023s5g7id62os2sgdsbr3xi6z5taiv6z6lz21rwkh0idjv390pd1c1a", "EG1k65p3lvryhywvwj5ff15yz3avankk6z5taiv6z6lz21rwkh0idjv390pd1c1a", "OF991dno2plyp2v7fvtjiceha2onwf6c6z5taiv6z6lz21rwkh0idjv390pd1c1a", "PR5lnvdrwsh1i01r5526bcvqu8c7ka4u6z5taiv6z6lz21rwkh0idjv390pd1c1a",
      "OH54iu1vzrdao3l2ueue3axyuv8w8htm6z5taiv6z6lz21rwkh0idjv390pd1c1a", "LF0j9yo9qam6ru438zv4jxoirhn1v1vz6z5taiv6z6lz21rwkh0idjv390pd1c1a", "FF3ekq7m3qvp8f7b241uo57qge8exxwq6z5taiv6z6lz21rwkh0idjv390pd1c1a", "HH1uin9h20rd9cp3erbdso8rxrobouz36z5taiv6z6lz21rwkh0idjv390pd1c1a", "JJ0mih0d90w74ozcfbwib77uuez1vhu16z5taiv6z6lz21rwkh0idjv390pd1c1a", "KK7d69xdp5686fijh7636sd30a5k55aa6z5taiv6z6lz21rwkh0idjv390pd1c1a",

      "z6mf0elsj3gpcj9bw59u091vWE3pdp4l6z5taiv6z6lz21rwkh0idjv390pd1c1a", "y09vb6ftinuchoiruu3hxmdrQ2K4w7vyf6z5aiv6z6lz21rwkh0idjv390pd1c1a", "05bpilbn6lueprawrvkbzxbh3o4LFj7n6z5taiv6z6lz21rwkh0idjv390pd1c1a", "qxqxdw6esnummho0ri67hw8schpLDqf66z5taiv6z6lz21rwkh0idjv390pd1c1a", "73p5vkmq655suciijth4c8g7i0dLW7ld6z5taiv6z6lz21rwkh0idjv390pd1c1a", "8ctmf1299sv9vujnfx1mamkqo87PQwpn6z5taiv6z6lz21rwkh0idjv390pd1c1a", "xbs28wb11jrg1dxun78u330jct9OHe286z5taiv6z6lz21rwkh0idjv390pd1c1a", "xddk39nkqyc7jlkzngbirk6uurOD9xxc6z5taiv6z6lz21rwkh0idjv390pd1c1a", "2dukybp6urKDhdypvnppomeynzy4yvld6z5taiv6z6lz21rwkh0idjv390pd1c1a", "qlxntb8uij0KG7g07nc4761o8xqmebmv6z5taiv6z6lz21rwkh0idjv390pd1c1a", "0c80qnlil9gsLFt0ulqjmzx6lh4ddiqz6z5taiv6z6lz21rwkh0idjv390pd1c1a",
      "282i4u611aet0PDw4axc77dv7htol6h06z5taiv6z6lz21rwkh0idjv390pd1c1a", "xazfteinusa20lOHhjaeiy7ilg31rm736z5taiv6z6lz21rwkh0idjv390pd1c1a", "0o6mmxu34ntopjkxp93FIqydj9izlgj36z5taiv6z6lz21rwkh0idjv390pd1c1a", "sa5ted63cv5s9f86ztrrg3EOdlcdgqk26z5taiv6z6lz21rwkh0idjv390pd1c1a", "xeeyxo1mh2cfk3evd4kqyl2OFefxfby86z5taiv6z6lz21rwkh0idjv390pd1c1a", "77is2OFu25848w8pwyfngbvhjdatd75g6z5taiv6z6lz21rwkh0idjv390pd1c1a", "qqpmcOA48bljrh7pcpd5rw2mnkba6hvo6z5taiv6z6lz21rwkh0idjv390pd1c1a", "8eqleQQa9drmhv3zg83f8iwiup3c4k406z5taiv6z6lz21rwkh0idjv390pd1c1a", "0ltbbQWwscmb6w3sfa47keg44yptr0vf6z5taiv6z6lz21rwkh0idjv390pd1c1a", "x9zqrQE9c2pkvh0utfgubg6tkv8qbzpo6z5taiv6z6lz21rwkh0idjv390pd1c1a", "08305QRzf6hah9lq8o5t86jpoclunr4n6z5taiv6z6lz21rwkh0idjv390pd1c1a",
      "8csw7QTk7w12p0cykv0iok3xs5pbro1m6z5taiv6z6lz21rwkh0idjv390pd1c1a", "xgmipQYafifa2nn96ecmse3pesfaughm6z5taiv6z6lz21rwkh0idjv390pd1c1a", "sk0p7AAxvdzjcz0liqkdxhtid3soo7i16z5taiv6z6lz21rwkh0idjv390pd1c1a", "sgo6dAS1cl0slk8pga3xns294gqdsnfo6z5taiv6z6lz21rwkh0idjv390pd1c1a", "xmcd4AD1vur1mgzde1287pbdocxq0kdu6z5taiv6z6lz21rwkh0idjv390pd1c1a", "xa5rvAF9g6mapu6l1f7929lp2nbewggu6z5taiv6z6lz21rwkh0idjv390pd1c1a", "72qvfAGmgsz87929uyz3hovifsn0utc86z5taiv6z6lz21rwkh0idjv390pd1c1a", "7bnqfAHyon0jcfb05e9ndov27pmiws7q6z5taiv6z6lz21rwkh0idjv390pd1c1a", "xp837AJyrrg2qj5bf7x5jaolhqiiapo56z5taiv6z6lz21rwkh0idjv390pd1c1a", "zk0z4ZZclszjbxc06y0li58z7uldmt1m6z5taiv6z6lz21rwkh0idjv390pd1c1a", "8eehmZXccltibnn79u380y34klfj5mx56z5taiv6z6lz21rwkh0idjv390pd1c1a",
      "0b4ceZCikxvftc2y17xv1pz3kc5qvzd26z5taiv6z6lz21rwkh0idjv390pd1c1a", "qzcn0ZVtl9cyoyo34yc16itff4yolm5u6z5taiv6z6lz21rwkh0idjv390pd1c1a", "0n4llZBgp0v86difh35jfapay30k1kit6z5taiv6z6lz21rwkh0idjv390pd1c1a", "qx8x1ZN46hzd03ddqkeluph56cnhjvqd6z5taiv6z6lz21rwkh0idjv390pd1c1a", "8eos8ZMqfqzfw89ueu39rsmqzsporcvt6z5taiv6z6lz21rwkh0idjv390pd1c1a", "0p93cZP9fi8j49citso5tzcfirolwl136z5taiv6z6lz21rwkh0idjv390pd1c1a", "qyklnPP14isgu7hrjb5qrdkygc71neye6z5taiv6z6lz21rwkh0idjv390pd1c1a", "r1umuPOej023s5g7id62os2sgdsbr3xi6z5taiv6z6lz21rwkh0idjv390pd1c1a", "711k6PI3lvryhywvwj5ff15yz3avankk6z5taiv6z6lz21rwkh0idjv390pd1c1a", "6y991PUo2plyp2v7fvtjiceha2onwf6c6z5taiv6z6lz21rwkh0idjv390pd1c1a", "xv5lnPYrwsh1i01r5526bcvqu8c7ka4u6z5taiv6z6lz21rwkh0idjv390pd1c1a",
      "0r54iOIvzrdao3l2ueue3axyuv8w8htm6z5taiv6z6lz21rwkh0idjv390pd1c1a", "xx0j9OO9qam6ru438zv4jxoirhn1v1vz6z5taiv6z6lz21rwkh0idjv390pd1c1a", "st3ekOYm3qvp8f7b241uo57qge8exxwq6z5taiv6z6lz21rwkh0idjv390pd1c1a", "su1uiOTh20rd9cp3erbdso8rxrobouz36z5taiv6z6lz21rwkh0idjv390pd1c1a", "0c0miORd90w74ozcfbwib77uuez1vhu16z5taiv6z6lz21rwkh0idjv390pd1c1a", "z17d6OEdp5686fijh7636sd30a5k55aa6z5taiv6z6lz21rwkh0idjv390pd1c1a" };

  private static CountDownLatch lock;

  private void waitOnThreads()
  {
    try
    {
      lock.await();
    }
    catch (InterruptedException e)
    {
      throw new RuntimeException(e);
    }
  }

  private void waitRandomAmount()
  {
    Object threadWait = new Object();

    synchronized (threadWait)
    {
      try
      {
        threadWait.wait(new Random().nextInt(2) + 70);
      }
      catch (InterruptedException e)
      {
        throw new RuntimeException(e);
      }
    }
  }

  private void onThreadStart(String mobileId)
  {
    waitRandomAmount();

    String returnedMobileId = idConverter.getMobileIdFromSessionId((String) mobileIdToSessionId.get(mobileId));
    String returnedSessionId = idConverter.getSessionIdFromMobileId(mobileId);

    Assert.assertEquals(mobileId, returnedMobileId);
    Assert.assertEquals(mobileIdToSessionId.get(mobileId), returnedSessionId);
  }

  private void onThreadEnd()
  {
    lock.countDown();
  }

  @Request
  @Test
  public void testConvertGlobalAndLocalIds()
  {
    mobileIdToSessionId = new DualHashBidiMap();

    // Maps a session to a bi-directional hash map BidiMap<globalId, localId>
    final HashMap<String, BidiMap> idMap = new HashMap<String, BidiMap>();

    // Log all our threads in, and map the session ids to our mobile ids.
    // Do it twice so that we can ensure we can update an existing sessionId
    // mapping with no problem.
    for (int i = 0; i < 2; ++i)
    {
      lock = new CountDownLatch(mobileIds.length);
      for (final String mobileId : mobileIds)
      {
        Thread t = new Thread()
        {
          public void run()
          {
            idConverter = IdConverter.getInstance();
            ClientSession clientSession = ClientSession.createUserSession("default", customUsernameAndPassword, customUsernameAndPassword, new Locale[] { CommonProperties.getDefaultLocale() });

            mobileIdToSessionId.put(mobileId, clientSession.getSessionId());

            idConverter.mapSessionIdToMobileId(clientSession.getSessionId(), mobileId);

            onThreadStart(mobileId);

            onThreadEnd();
            clientSession.logout();
          }
        };
        t.setDaemon(true);
        t.start();
      }
      waitOnThreads();
    }
    System.out.println("Session ids mapped to mobile ids.");

    // Generate a bunch of local ids
    lock = new CountDownLatch(mobileIds.length);
    for (final String mobileId : mobileIds)
    {
      Thread t = new Thread()
      {
        public void run()
        {
          onThreadStart(mobileId);

          BidiMap globalIdToLocalIdMap = new DualHashBidiMap();
          idMap.put(mobileId, globalIdToLocalIdMap);

          for (String globalId : globalIds)
          {
            String localId = idConverter.generateLocalIdFromGlobalId(mobileId, globalId);
            globalIdToLocalIdMap.put(globalId, localId);

            // System.out.println(mobileId + "\n" + globalId + "\n" + localId +
            // "\n\n");
          }

          onThreadEnd();
        }
      };
      t.setDaemon(true);
      t.start();
    }
    waitOnThreads();
    System.out.println("localIds generated and mapped to globalIds");

    // Assert that we can get the globalId back from the localId
    lock = new CountDownLatch(mobileIds.length);
    for (final String mobileId : mobileIds)
    {
      Thread t = new Thread()
      {
        @SuppressWarnings("unchecked")
        public void run()
        {
          onThreadStart(mobileId);

          BidiMap globalIdToLocalIdMap = idMap.get(mobileId);
          Collection<String> localIds = globalIdToLocalIdMap.values();

          for (String localId : localIds)
          {
            String globalId = idConverter.getGlobalIdFromLocalId(mobileId, localId);
            Assert.assertEquals((String) globalIdToLocalIdMap.getKey(localId), globalId);
          }

          onThreadEnd();
        }
      };
      t.setDaemon(true);
      t.start();
    }
    waitOnThreads();
    System.out.println("globalIds retrieved from localIds.");

    // Generate local ids from the same mobile ids again and make sure we got
    // the same local ids
    lock = new CountDownLatch(mobileIds.length);
    for (final String mobileId : mobileIds)
    {
      Thread t = new Thread()
      {
        public void run()
        {
          onThreadStart(mobileId);

          BidiMap globalIdToLocalIdMap = idMap.get(mobileId);

          for (String globalId : globalIds)
          {
            String localId = idConverter.generateLocalIdFromGlobalId(mobileId, globalId);
            Assert.assertEquals(localId, (String) globalIdToLocalIdMap.get(globalId));
          }

          onThreadEnd();
        }
      };
      t.setDaemon(true);
      t.start();
    }
    waitOnThreads();
    System.out.println("Generated local ids from the same mobile ids.");

    // Delete all the global ids
    idConverter = IdConverter.getInstance();
    lock = new CountDownLatch(globalIds.length);
    for (final String globalId : globalIds)
    {
      Thread t = new Thread()
      {
        public void run()
        {
          waitRandomAmount();
          // System.out.println("Invalidating globalId " + globalId);

          idConverter.invalidateGlobalId(globalId);

          onThreadEnd();
        }
      };
      t.setDaemon(true);
      t.start();
    }
    waitOnThreads();
    System.out.println("Deleted all global ids.");

    // Try to retrieve them and assert that they're null.
    lock = new CountDownLatch(mobileIds.length);
    for (final String mobileId : mobileIds)
    {
      Thread t = new Thread()
      {
        public void run()
        {
          onThreadStart(mobileId);

          BidiMap globalIdToLocalIdMap = idMap.get(mobileId);

          for (String globalId : globalIds)
          {
            try
            {
              String retval = idConverter.getGlobalIdFromLocalId(mobileId, (String) globalIdToLocalIdMap.get(globalId));
              Assert.fail("The globalId is still mapped. globalId = " + retval);
            }
            catch (IdConversionException e)
            {
            }
          }

          onThreadEnd();
        }
      };
      t.setDaemon(true);
      t.start();
    }
    waitOnThreads();
    System.out.println("Tried to retrieve previously deleted global ids.");

    // Generate more localIds, just to flex the stacks
    lock = new CountDownLatch(mobileIds.length);
    for (final String mobileId : mobileIds)
    {
      Thread t = new Thread()
      {
        public void run()
        {
          onThreadStart(mobileId);

          BidiMap globalIdToLocalIdMap = new DualHashBidiMap();
          idMap.put(mobileId, globalIdToLocalIdMap);

          for (String globalId : globalIds)
          {
            String localId = idConverter.generateLocalIdFromGlobalId(mobileId, globalId);
            globalIdToLocalIdMap.put(globalId, localId);
          }

          onThreadEnd();
        }
      };
      t.setDaemon(true);
      t.start();
    }
    waitOnThreads();
    System.out.println("Generated more localIds, to flex the stacks.");

    // Assert that we can get the globalId back from the localId
    lock = new CountDownLatch(mobileIds.length);
    for (final String mobileId : mobileIds)
    {
      Thread t = new Thread()
      {
        @SuppressWarnings("unchecked")
        public void run()
        {
          onThreadStart(mobileId);

          BidiMap globalIdToLocalIdMap = idMap.get(mobileId);
          Collection<String> localIds = globalIdToLocalIdMap.values();

          for (String localId : localIds)
          {
            String globalId = idConverter.getGlobalIdFromLocalId(mobileId, localId);
            Assert.assertEquals((String) globalIdToLocalIdMap.getKey(localId), globalId);
          }

          onThreadEnd();
        }
      };
      t.setDaemon(true);
      t.start();
    }
    waitOnThreads();
    System.out.println("globalids retrieved from localids.");

    // Delete all the global ids
    idConverter = IdConverter.getInstance();
    lock = new CountDownLatch(globalIds.length);
    for (final String globalId : globalIds)
    {
      Thread t = new Thread()
      {
        public void run()
        {
          waitRandomAmount();
          // System.out.println("Invalidating globalId " + globalId);

          idConverter.invalidateGlobalId(globalId);

          onThreadEnd();
        }
      };
      t.setDaemon(true);
      t.start();
    }
    waitOnThreads();
    System.out.println("Deleted all global oid, test complete.");
  }
}

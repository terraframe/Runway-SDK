/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.web.view.html;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.text.translate.CharSequenceTranslator;
import org.apache.commons.text.translate.NumericEntityEscaper;

public class EscapeUtil
{
  /**
   * HTML 4 named entities, plus numeric entities for any other non-ASCII character. This matches the output
   * of commons-lang 2's StringEscapeUtils.escapeHtml(); commons-text's escapeHtml4() alone would leave those
   * characters unescaped.
   */
  private static final CharSequenceTranslator ESCAPE_HTML = StringEscapeUtils.ESCAPE_HTML4.with(NumericEntityEscaper.above(0x7f));
  
  /**
   * Escapes HTML.
   * 
   * @param html
   * @return
   */
  public static final String escapeHTML(String html)
  {
    return ESCAPE_HTML.translate(html);
  }
  
  /**
   * Escapes Javascript.
   * 
   * @param js
   * @return
   */
  public static final String escapeJS(String js)
  {
    return StringEscapeUtils.escapeEcmaScript(js);
  }
  
  /**
   * Escapes both HTML and Javascript
   * 
   * @param html
   * @return
   */
  public static final String escapeHTMLAndJS(String html)
  {
    html = ESCAPE_HTML.translate(html);
    html = StringEscapeUtils.escapeEcmaScript(html);
    return html;
  }
}

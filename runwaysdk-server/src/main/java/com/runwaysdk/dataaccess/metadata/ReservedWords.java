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
package com.runwaysdk.dataaccess.metadata;

import java.text.Normalizer;
import java.util.HashSet;

import org.apache.commons.lang.StringUtils;

/**
 * ReservedWords contains a list of restricted words (generally database related) that other
 * user defined words cannot be.
 */
public class ReservedWords
{
  /**
   * The sets of reserved words.
   */
  private HashSet<String> columnWords;
  private HashSet<String> attributeWords;
  
  private static class Singleton
  {
    private static ReservedWords Instance = new ReservedWords();
  }

  /**
   * Constructor
   */
  private ReservedWords()
  {
	  // Reserved column names, from database languages: SQL Server, MySQL, PostgreSQL, and Oracle
	  
	String[] array = { 
			// SQL Server reserved keywords: http://msdn.microsoft.com/en-us/library/aa238507%28SQL.80%29.aspx
	        /*
			"add", "except", "percent",
			"all", "exec", "plan",
			"alter", "execute", "precision",
			"and", "exists", "primary",
			"any", "exit", "print",
			"as", "fetch", "proc",
			"asc", "file", "procedure",
			"authorization", "fillfactor", "public",
			"backup", "for", "raiserror",
			"begin", "foreign", "read",
			"between", "freetext", "readtext",
			"break", "freetexttable", "reconfigure",
			"browse", "from", "references",
			"bulk", "full", "replication",
			"by", "function", "restore",
			"cascade", "goto", "restrict",
			"case", "grant", "return",
			"check", "group", "revoke",
			"checkpoint", "having", "right",
			"close", "holdlock", "rollback",
			"clustered", "identity", "rowcount",
			"coalesce", "identity_insert", "rowguidcol",
			"collate", "identitycol", "rule",
			"column", "if", "save",
			"commit", "in", "schema",
			"compute", "index", "select",
			"constraint", "inner", "session_user",
			"contains", "insert", "set",
			"containstable", "intersect", "setuser",
			"continue", "into", "shutdown",
			"convert", "is", "some",
			"create", "join", "statistics",
			"cross", "key", "system_user",
			"current", "kill", "table",
			"current_date", "left", "textsize",
			"current_time", "like", "then",
			"current_timestamp", "lineno", "to",
			"current_user", "load", "top",
			"cursor", "national", "tran",
			"database", "nocheck", "transaction",
			"dbcc", "nonclustered", "trigger",
			"deallocate", "not", "truncate",
			"declare", "null", "tsequal",
			"default", "nullif", "union",
			"delete", "of", "unique",
			"deny", "off", "update",
			"desc", "offsets", "updatetext",
			"disk", "on", "use",
			"distinct", "open", "user",
			"distributed", "opendatasource", "values",
			"double", "openquery", "varying",
			"drop", "openrowset", "view",
			"dummy", "openxml", "waitfor",
			"dump", "option", "when",
			"else", "or", "where",
			"end", "order", "while",
			"errlvl", "outer", "with",
			"escape", "over", "writetext",
			*/
			
			//SQL Server ODBC Reserved Keywords
			/*
			"absolute", "exec", "overlaps",
			"action", "execute", "pad",
			"ada", "exists", "partial",
			"add", "external", "pascal",
			"all", "extract", "position",
			"allocate", "false", "precision",
			"alter", "fetch", "prepare",
			"and", "first", "preserve",
			"any", "float", "primary",
			"are", "for", "prior",
			"as", "foreign", "privileges",
			"asc", "fortran", "procedure",
			"assertion", "found", "public",
			"at", "from", "read",
			"authorization", "full", "real",
			"avg", "get", "references",
			"begin", "global", "relative",
			"between", "go", "restrict",
			"bit", "goto", "revoke",
			"bit_length", "grant", "right",
			"both", "group", "rollback",
			"by", "having", "rows",
			"cascade", "hour", "schema",
			"cascaded", "identity", "scroll",
			"case", "immediate", "second",
			"cast", "in", "section",
			"catalog", "include", "select",
			"char", "index", "session",
			"char_length", "indicator", "session_user",
			"character", "initially", "set",
			"character_length", "inner", "size",
			"check", "input", "smallint",
			"close", "insensitive", "some",
			"coalesce", "insert", "space",
			"collate", "int", "sql",
			"collation", "integer", "sqlca",
			"column", "intersect", "sqlcode",
			"commit", "interval", "sqlerror",
			"connect", "into", "sqlstate",
			"connection", "is", "sqlwarning",
			"constraint", "isolation", "substring",
			"constraints", "join", "sum",
			"continue", "key", "system_user",
			"convert", "language", "table",
			"corresponding", "last", "temporary",
			"count", "leading", "then",
			"create", "left", "time",
			"cross", "level", "timestamp",
			"current", "like", "timezone_hour",
			"current_date", "local", "timezone_minute",
			"current_time", "lower", "to",
			"current_timestamp", "match", "trailing",
			"current_user", "max", "transaction",
			"cursor", "min", "translate",
			"date", "minute", "translation",
			"day", "module", "trim",
			"deallocate", "month", "true",
			"dec", "names", "union",
			"decimal", "national", "unique",
			"declare", "natural", "unknown",
			"default", "nchar", "update",
			"deferrable", "next", "upper",
			"deferred", "no", "usage",
			"delete", "none", "user",
			"desc", "not", "using",
			"describe", "null", "value",
			"descriptor", "nullif", "values",
			"diagnostics", "numeric", "varchar",
			"disconnect", "octet_length", "varying",
			"distinct", "of", "view",
			"domain", "on", "when",
			"double", "only", "whenever",
			"drop", "open", "where",
			"else", "option", "with",
			"end", "or", "work",
			"end-exec", "order", "write",
			"escape", "outer", "year",
			"except", "output", "zone",
			"exception",
			*/
			
			// SQL Server Future Keywords:
			/*
			"absolute", "found", "preserve",
			"action", "free", "prior",
			"admin", "general", "privileges",
			"after", "get", "reads",
			"aggregate", "global", "real",
			"alias", "go", "recursive",
			"allocate", "grouping", "ref",
			"are", "host", "referencing",
			"array", "hour", "relative",
			"assertion", "ignore", "result",
			"at", "immediate", "returns",
			"before", "indicator", "role",
			"binary", "initialize", "rollup",
			"bit", "initially", "routine",
			"blob", "inout", "row",
			"boolean", "input", "rows",
			"both", "int", "savepoint",
			"breadth", "integer", "scroll",
			"call", "interval", "scope",
			"cascaded", "isolation", "search",
			"cast", "iterate", "second",
			"catalog", "language", "section",
			"char", "large", "sequence",
			"character", "last", "session",
			"class", "lateral", "sets",
			"clob", "leading", "size",
			"collation", "less", "smallint",
			"completion", "level", "space",
			"connect", "limit", "specific",
			"connection", "local", "specifictype",
			"constraints", "localtime", "sql",
			"constructor", "localtimestamp", "sqlexception",
			"corresponding", "locator", "sqlstate",
			"cube", "map", "sqlwarning",
			"current_path", "match", "start",
			"current_role", "minute", "state",
			"cycle", "modifies", "statement",
			"data", "modify", "static",
			"date", "module", "structure",
			"day", "month", "temporary",
			"dec", "names", "terminate",
			"decimal", "natural", "than",
			"deferrable", "nchar", "time",
			"deferred", "nclob", "timestamp",
			"depth", "new", "timezone_hour",
			"deref", "next", "timezone_minute",
			"describe", "no", "trailing",
			"descriptor", "none", "translation",
			"destroy", "numeric", "treat",
			"destructor", "object", "true",
			"deterministic", "old", "under",
			"dictionary", "only", "unknown",
			"diagnostics", "operation", "unnest",
			"disconnect", "ordinality", "usage",
			"domain", "out", "using",
			"dynamic", "output", "value",
			"each", "pad", "varchar",
			"end-exec", "parameter", "variable",
			"equals", "parameters", "whenever",
			"every", "partial", "without",
			"exception", "path", "work",
			"external", "postfix", "write",
			"false", "prefix", "year",
			"first", "preorder", "zone",
			"float", "prepare",
			*/
			
			// MySQL: http://dev.mysql.com/doc/refman/5.0/en/reserved-words.html
			/*
			"add", "all", "alter",
			"analyze", "and", "as",
			"asc", "asensitive", "before",
			"between", "bigint", "binary",
			"blob", "both", "by",
			"call", "cascade", "case",
			"change", "char", "character",
			"check", "collate", "column",
			"condition", "constraint", "continue",
			"convert", "create", "cross",
			"current_date", "current_time", "current_timestamp",
			"current_user", "cursor", "database",
			"databases", "day_hour", "day_microsecond",
			"day_minute", "day_second", "dec",
			"decimal", "declare", "default",
			"delayed", "delete", "desc",
			"describe", "deterministic", "distinct",
			"distinctrow", "div", "double",
			"drop", "dual", "each",
			"else", "elseif", "enclosed",
			"escaped", "exists", "exit",
			"explain", "false", "fetch",
			"float", "float4", "float8",
			"for", "force", "foreign",
			"from", "fulltext", "grant",
			"group", "having", "high_priority",
			"hour_microsecond", "hour_minute", "hour_second",
			"if", "ignore", "in",
			"index", "infile", "inner",
			"inout", "insensitive", "insert",
			"int", "int1", "int2",
			"int3", "int4", "int8",
			"integer", "interval", "into",
			"is", "iterate", "join",
			"key", "keys", "kill",
			"leading", "leave", "left",
			"like", "limit", "lines",
			"load", "localtime", "localtimestamp",
			"lock", "long", "longblob",
			"longtext", "loop", "low_priority",
			"match", "mediumblob", "mediumint",
			"mediumtext", "middleint", "minute_microsecond",
			"minute_second", "mod", "modifies",
			"natural", "not", "no_write_to_binlog",
			"null", "numeric", "on",
			"optimize", "option", "optionally",
			"or", "order", "out",
			"outer", "outfile", "precision",
			"primary", "procedure", "purge",
			"read", "reads", "real",
			"references", "regexp", "release",
			"rename", "repeat", "replace",
			"require", "restrict", "return",
			"revoke", "right", "rlike",
			"schema", "schemas", "second_microsecond",
			"select", "sensitive", "separator",
			"set", "show", "smallint",
			"soname", "spatial", "specific",
			"sql", "sqlexception", "sqlstate",
			"sqlwarning", "sql_big_result", "sql_calc_found_rows",
			"sql_small_result", "ssl", "starting",
			"straight_join", "table", "terminated",
			"then", "tinyblob", "tinyint",
			"tinytext", "to", "trailing",
			"trigger", "true", "undo",
			"union", "unique", "unlock",
			"unsigned", "update", "usage",
			"use", "using", "utc_date",
			"utc_time", "utc_timestamp", "values",
			"varbinary", "varchar", "varcharacter",
			"varying", "when", "where",
			"while", "with", "write",
			"xor", "year_month", "zerofill",
			*/
			
			// MySQL 5.0
			/*
			"asensitive", "call", "condition",
			"connection", "continue", "cursor",
			"declare", "deterministic", "each",
			"elseif", "exit", "fetch",
			"goto", "inout", "insensitive",
			"iterate", "leave",
			"loop", "modifies", "out",
			"reads", "release", "repeat",
			"return", "schema", "schemas",
			"sensitive", "specific", "sql",
			"sqlexception", "sqlstate", "sqlwarning",
			"trigger", "undo", "upgrade",
			"while",
			*/
			
			// PostgreSQL: http://www.postgresql.org/docs/7.3/static/sql-keywords-appendix.html
			"admin",
		    "all",
			"analyse",
			"analyze", "and", "any",
			"as",
			"asc",
			"authorization",
			"between", "binary",
			"both",
			"by",
			"call",
			"case", "cast",
		    "check",
			"collate",
			"column", "constraint", "create",
			"cross",
			"current_date",
			"current_time",
			"current_timestamp", "current_user",
			"default", "deferrable", "desc", "dispatch", "distinct",
			"do", "domain", "each", "else",
			"end",
			"escape", "except",
			"exclusive", "exec", "false",
			"fetch", "for",
			"foreign", "freeze",
			"from", "full", "grant",
			"group",
			"having", "hold", "ilike",
			"in", "initially", "inner",
			"intersect", "into",
			"is", "isnull",
			"join",
			"leading", "left", "like",
			"limit", "localtime", "localtimestamp",
		    "natural", "new",
			"none", "not", "notnull", "null",
		    "off", "offset",
			"old", "on",
			"only", "options",
			"or", "order", "outer",
			"overlaps",
			"placing", "preserve",
			"primary",
			"references", "right", "select",
			"session_user",
			"similar", "some",
			"then",
			"to", "trailing",
		    "true",
			"union", "unique", "user", "using",
		    "verbose",
			"when", "where",
			
			// Oracle Reserved Words: http://download.oracle.com/docs/cd/B10500_01/appdev.920/a42525/apb.htm
			/*
			"access", "else", "modify", "start",
			"add", "exclusive", "noaudit", "select",
			"all", "exists", "nocompress", "session",
			"alter", "file", "not", "set",
			"and", "float", "notfound", "share",
			"any", "for", "nowait", "size",
			"arraylen", "from", "null", "smallint",
			"as", "grant", "number", "sqlbuf",
			"sac", "group", "of", "successful",
			"audit", "having", "offline", "synonym",
			"between", "identified", "on", "sysdate",
			"by", "immediate", "online", "table",
			"char", "in", "option", "then",
			"check", "increment", "or", "to",
			"cluster", "index", "order", "trigger",
			"column", "initial", "pctfree", "uid",
			"comment", "insert", "prior", "union",
			"compress", "integer", "privileges", "unique",
			"connect", "intersect", "public", "update",
			"create", "into", "raw", "user",
			"current", "is", "rename", "validate",
			"date", "level", "resource", "values",
			"decimal", "like", "revoke", "varchar",
			"default", "lock", "row", "varchar2",
			"delete", "long", "rowid", "view",
			"desc", "maxextents", "rowlabel", "whenever",
			"distinct", "minus", "rownum", "where",
			"drop", "mode", "rows", "with",
			*/
			
			// Oracle Keywords: The following words also have a special meaning to Oracle but are not reserved words and so can be redefined. However, some might eventually become reserved words.
			/*
			"admin", "cursor", "found", "mount",
			"after", "cycle", "function", "next",
			"allocate", "database", "go", "new",
			"analyze", "datafile", "goto", "noarchivelog",
			"archive", "dba", "groups", "nocache",
			"archivelog", "dec", "including", "nocycle",
			"authorization", "declare", "indicator", "nomaxvalue",
			"avg", "disable", "initrans", "nominvalue",
			"backup", "dismount", "instance", "none",
			"begin", "double", "int", "noorder",
			"become", "dump", "key", "noresetlogs",
			"before", "each", "language", "normal",
			"block", "enable", "layer", "nosort",
			"body", "end", "link", "numeric",
			"cache", "escape", "lists", "off",
			"cancel", "events", "logfile", "old",
			"cascade", "except", "manage", "only",
			"change", "exceptions", "manual", "open",
			"character", "exec", "max", "optimal",
			"checkpoint", "explain", "maxdatafiles", "own",
			"close", "execute", "maxinstances", "package",
			"cobol", "extent", "maxlogfiles", "parallel",
			"commit", "externally", "maxloghistory", "pctincrease",
			"compile", "fetch", "maxlogmembers", "pctused",
			"constraint", "flush", "maxtrans", "plan",
			"constraints", "freelist", "maxvalue", "pli",
			"contents", "freelists", "min", "precision",
			"continue", "force", "minextents", "primary",
			"controlfile", "foreign", "minvalue", "private",
			"count", "fortran", "module", "procedure",
			"profile", "savepoint", "sqlstate", "tracing",
			"quota", "schema", "statement_id", "transaction",
			"read", "scn", "statistics", "triggers",
			"real", "section", "stop", "truncate",
			"recover", "segment", "storage", "under",
			"references", "sequence", "sum", "unlimited",
			"referencing", "shared", "switch", "until",
			"resetlogs", "snapshot", "system", "use",
			"restricted", "some", "tables", "using",
			"reuse", "sort", "tablespace", "when",
			"role", "sql", "temporary", "write",
			"roles", "sqlcode", "thread", "work",
			"rollback", "sqlerror", "time",
			*/
			
			// Oracle PL/SQL Reserved Words: The following PL/SQL keywords may require special treatment when used in embedded SQL statements.
			/*
			"abort", "between", "crash", "digits",
			"accept", "binary_integer", "create", "dispose",
			"access", "body", "current", "distinct",
			"add", "boolean", "currval", "do",
			"all", "by", "cursor", "drop",
			"alter", "case", "database", "else",
			"and", "char", "data_base", "elsif",
			"any", "char_base", "date", "end",
			"array", "check", "dba", "entry",
			"arraylen", "close", "debugoff", "exception",
			"as", "cluster", "debugon", "exception_init",
			"sac", "clusters", "declare", "exists",
			"assert", "colauth", "decimal", "exit",
			"assign", "columns", "default", "false",
			"at", "commit", "definition", "fetch",
			"authorization", "compress", "delay", "float",
			"avg", "connect", "delete", "for",
			"base_table", "constant", "delta", "form",
			"begin", "count", "desc", "from",
			"function", "new", "release", "sum",
			"generic", "nextval", "remr", "tabauth",
			"goto", "nocompress", "rename", "table",
			"grant", "not", "resource", "tables",
			"group", "null", "return", "task",
			"having", "number", "reverse", "terminate",
			"identified", "number_base", "revoke", "then",
			"if", "of", "rollback", "to",
			"in", "on", "rowid", "true",
			"index", "open", "rowlabel", "type",
			"indexes", "option", "rownum", "union",
			"indicator", "or", "rowtype", "unique",
			"insert", "order", "run", "update",
			"integer", "others", "savepoint", "use",
			"intersect", "out", "schema", "values",
			"into", "package", "select", "varchar",
			"is", "partition", "separate", "varchar2",
			"level", "pctfree", "set", "variance",
			"like", "positive", "size", "view",
			"limited", "pragma", "smallint", "views",
			"loop", "prior", "space", "when",
			"max", "private", "sql", "where",
			"min", "procedure", "sqlcode", "while",
			"minus", "public", "sqlerrm", "with",
			"mislabel", "raise", "start", "work",
			"mod", "range", "statement", "xor",
			"mode", "real", "stddev",
			"natural", "record", "subtype"
			*/
	};

    columnWords = new HashSet<String>(array.length);
    for(String word: array)
    {
      columnWords.add(word.toLowerCase());
    }
    
    // Reserved attribute names

    String[] attributeArray = {
    		// Java
    		"abstract", "continue", "for",
    		"new", "switch", "assert",
    		"default", "goto", "package",
    		"synchronized", "boolean", "do",
    		"if", "private", "this",
    		"break", "double", "implements",
    		"protected", "throw", "byte",
    		"else", "import", "public",
    		"throws", "case", "enum",
    		"instanceof", "return", "transient",
    		"catch", "extends", "int",
    		"short", "try", "char",
    		"final", "interface", "static",
    		"void", "class", "finally",
    		"long", "strictfp", "volatile",
    		"const", "float", "native",
    		"super", "while",
    		
    		// Javascript
    		"abstract", "else", "instanceof",
    		"switch", "boolean", "enum",
    		"int", "synchronized", "break",
    		"export", "interface", "this",
    		"byte extends long",
    		"throw", "case", "false",
    		"native", "throws", "catch",
    		"final", "new", "transient",
    		"char finally", "null",
    		"true", "class", "float",
    		"package", "try", "const",
    		"for", "private", "typeof",
    		"continue", "function", "protected",
    		"var", "debugger", "goto",
    		"public", "void", "default",
    		"if", "return", "volatile",
    		"delete", "implements", "short",
    		"while", "do", "import",
    		"static", "with", "double",
    		"in", "super"
    };
    
    attributeWords = new HashSet<String>(attributeArray.length);
    for(String word: attributeArray)
    {
      attributeWords.add(word.toLowerCase());
    }
  }

  /**
   * Returns true if the string specified in the parameter is found
   * in the set of strings held by this.reservedWords.
   *
   * @param find the string to find
   * @return true if the string is found, false otherwise.
   */
  public static synchronized boolean sqlContains(String find)
  {
    return Singleton.Instance.columnWords.contains(find.toLowerCase());
  }

  /**
   * 
   * @param find
   * @return
   */
  public static synchronized boolean javaContains(String find)
  {
	return Singleton.Instance.attributeWords.contains(find.toLowerCase());
  }
  
  public static void main(String[] args) {
//    String dirty = "wiojWEJI`1234567890-=~!@#$%^&*()_+[]\\{}|;':\",./<>?";
    String dirty = "clean";
    
    System.out.println(sanitizeForDatabaseIdentifer(dirty));
  }
  
  /**
   * Takes in a dirty, (potentially user provided) string and converts it into something which will be a valid postgres identifier, for use in
   * table names, etc.
   * 
   * @param dirty
   * @return
   */
  public static String sanitizeForDatabaseIdentifer(String dirty)
  {
    final int POSTGRES_MAX_TABLE_NAME_LEN = 63;
    
    String clean = StringUtils.deleteWhitespace(dirty);
    
    clean = Normalizer.normalize(clean, Normalizer.Form.NFD); // If its got a weird accent or something then replace it with one that doesn't
    clean = clean.replaceAll("(?=\\D)(?=(?![a-z]))(?=(?![A-Z])).", ""); // Just numbers and letters. Get rid of the weird stuff
    clean = clean.toLowerCase();
    
    if (clean.length() > POSTGRES_MAX_TABLE_NAME_LEN)
    {
      clean = clean.substring(0, POSTGRES_MAX_TABLE_NAME_LEN);
    }
    
    if (sqlContains(clean))
    {
      clean = clean.substring(0, clean.length()-1);
      clean = clean + "4";
    }
    
    return clean;
  }
}

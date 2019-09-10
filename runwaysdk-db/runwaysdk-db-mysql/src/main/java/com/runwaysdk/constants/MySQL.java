/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.constants;

import java.util.Hashtable;
import java.util.ListResourceBundle;

/**
 * Contains static properties for operation of the core with mysql. Login info
 * is specified in database.properties. Typical default values for logging in to
 * mysql are:
 * 
 * <pre>
 * port=3306
 * databaseName=runwaydb
 * user=runway
 * password=runway
 * rootuser=root
 * rootpassword=root
 * rootdatabase=mysql
 * </pre>
 * 
 * @author Eric
 */
public class MySQL extends ListResourceBundle
{
  public static final String NAME = "Mysql";

  @Override
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
      { "time.format", "HH:mm:ss" },
      { "date.format", "yyyy-MM-dd" },
      { "datetime.format", "yyyy-MM-dd HH:mm:ss" },
      { "attributeTypes", makeTable() },
      {
      "dbErrorCodes",
      "1000,1001,1002,1003,1004,1005,1006,1007,1008,1009,1010,1011,1012,1013,1014,1015,1016,1017,1018,1019,1021,1023,1024,1026,1028,1030,1034,1035,1036,1037,1038,1039,1040,1041,1042,1043,1044,1045,1046,1049,1081,1085,1086,1098,1102,1114,1115,1116,1117,1119,1122,1123,1124,1125,1126,1127,1129,1130,1131,1132,1133,1135,1137,1141,1142,1143,1144,1145,1147,1150,1151,1152,1153,1154,1155,1156,1157,1158,1159,1160,1161,1162,1174,1182,1184,1185,1186,1187,1188,1189,1190,1193,1194,1195,1197,1198,1199,1200,1201,1202,1203,1211,1218,1219,1220,1224,1226,1227,1228,1229,1236,1237,1244,1248,1251,1254,1255,1256,1257,1258,1259,1266,1268,1269,1274,1275,1277,1278,1279,1281,1282,1285,1286,1288,1289,1290,1298,1301,1303,1304,1305,1306,1307,1308,1309,1310,1311,1312,1313,1314,1315,1316,1318,1319,1320,1321,1322,1323,1324,1325,1326,1327,1328,1329,1330,1331,1332,1333,1334,1335,1336,1337,1338,1339,1340,1341,1342,1343,1344,1345,1346,1349,1350,1351,1352,1353,1354,1355,1356,1357,1358,1359,1360,1361,1362,1363,1364,1365,1368,1369,1370,1371,1372,1373,1374,1375,1376,1377,1378,1379,1380,1381,1383,1384,1385,1386,1387,1388,1389,1390,1392,1393,1394,1395,1396,1397,1398,1399,1400,1401,1402,1403,1404,1405,1408,1409,1410,1411,1413,1414,1415,1416,1417,1418,1419,1420,1421,1422,1423,1424,1428,1429,1430,1431,1432,1433,1434,1435,1436,1437,1438,1440,1441,1442,1443,1444,1445,1447,1448,1449,1450,1453,1454,1455,1456,1457,1458,1459,1460" },
      {
      "seriousDBErrorCodes",
      "1020,1022,1025,1027,1029,1031,1032,1033,1047,1048,1050,1051,1052,1054,1055,1056,1057,1058,1059,1061,1063,1064,1065,1066,1067,1068,1071,1072,1074,1075,1080,1082,1083,1084,1087,1088,1089,1090,1091,1092,1093,1094,1095,1096,1097,1099,1100,1101,1103,1104,1105,1106,1107,1108,1109,1110,1111,1112,1113,1118,1120,1121,1134,1136,1138,1139,1140,1146,1148,1149,1163,1164,1165,1166,1167,1168,1169,1170,1171,1172,1173,1175,1176,1177,1178,1179,1180,1181,1183,1191,1192,1196,1204,1205,1206,1207,1208,1209,1210,1212,1213,1214,1215,1216,1217,1221,1222,1223,1225,1230,1231,1232,1233,1234,1235,1238,1239,1240,1241,1242,1243,1245,1246,1247,1249,1250,1252,1253,1260,1261,1262,1263,1264,1265,1267,1270,1271,1272,1273,1276,1280,1283,1284,1287,1291,1292,1293,1294,1295,1296,1297,1299,1300,1302,1347,1348,1366,1367,1382,1391,1406,1407,1412,1425,1426,1427,1439,1446,1451,1452" },
      { "databaseClass", com.runwaysdk.dataaccess.database.general.MySQL.class.getName() }, };

  /**
   * Creates the HashTable that maps core attribute types to database types
   * 
   * @return MdAttribute->DbType HashTable
   */
  private static Hashtable<String, String> makeTable()
  {
    Hashtable<String, String> hashtable = new Hashtable<String, String>();
    hashtable.put(MdAttributeBooleanInfo.CLASS, "int");
    hashtable.put(MdAttributeIntegerInfo.CLASS, "int");
    hashtable.put(MdAttributeLongInfo.CLASS, "bigint");
    hashtable.put(MdAttributeFloatInfo.CLASS, "float");
    hashtable.put(MdAttributeDoubleInfo.CLASS, "double");
    hashtable.put(MdAttributeDecimalInfo.CLASS, "decimal");
    hashtable.put(MdAttributeCharacterInfo.CLASS, "varchar");
    hashtable.put(MdAttributeFileInfo.CLASS, "varchar");
    hashtable.put(MdAttributeTextInfo.CLASS, "text");
    hashtable.put(MdAttributeClobInfo.CLASS, "longtext");
    hashtable.put(MdAttributeBlobInfo.CLASS, "longblob");
    hashtable.put(MdAttributeTimeInfo.CLASS, "time");
    hashtable.put(MdAttributeDateInfo.CLASS, "date");
    hashtable.put(MdAttributeDateTimeInfo.CLASS, "datetime");
    hashtable.put(MdAttributeReferenceInfo.CLASS, "char");
    hashtable.put(MdAttributeTermInfo.CLASS, "char");
    hashtable.put(MdAttributeEnumerationInfo.CLASS, "char");
    hashtable.put(MdAttributeMultiReferenceInfo.CLASS, "char");
    hashtable.put(MdAttributeMultiTermInfo.CLASS, "char");
    hashtable.put(MdAttributeIndicatorInfo.CLASS, "char");
    hashtable.put(MdAttributeStructInfo.CLASS, "char");
    hashtable.put(MdAttributeLocalCharacterInfo.CLASS, "char");
    hashtable.put(MdAttributeLocalTextInfo.CLASS, "char");
    hashtable.put(MdAttributeHashInfo.CLASS, "varchar");
    hashtable.put(MdAttributeSymmetricInfo.CLASS, "text");
    hashtable.put(MdAttributeUUIDInfo.CLASS, "uuid");

    return hashtable;
  }
}

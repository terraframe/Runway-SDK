INSERT INTO metadata_display_label ( default_locale, id, site_master, key_name) VALUES ('false', 'qntlqfr84a8cexl2fasiyxvl8sj3czsqNM200904120000000000000000000030', 'www.runwaysdk.com', 'qntlqfr84a8cexl2fasiyxvl8sj3czsqNM200904120000000000000000000030');
INSERT INTO metadata_display_label ( default_locale, id, site_master, key_name) VALUES ('true', 'zjyk3zfcynantyza2wk3piwizq0q59i5NM200904120000000000000000000030', 'www.runwaysdk.com', 'zjyk3zfcynantyza2wk3piwizq0q59i5NM200904120000000000000000000030');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('xgm7mb0enjtvulez3he043ywyybiagvp', '20071109NM000000000000000000000520071109NM0000000000000000000001');
INSERT INTO metadata_display_label ( default_locale, id, site_master, key_name) VALUES ('Is Expression Attribute', '6ybo6hv9wpni79tbowb8r1dpcnsasxmrNM200904120000000000000000000030', 'www.runwaysdk.com', '6ybo6hv9wpni79tbowb8r1dpcnsasxmrNM200904120000000000000000000030');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('r7o8p1i49g3p9388po6hgbsgwf50cyqf', '20071109NM000000000000000000000520071109NM0000000000000000000001');
INSERT INTO md_attribute_indicies (set_id, item_id)  VALUES  ('sbzg7gbnlap3r0njwl8fuap2dupt3mns', '0000000000000000000000000000040100000000000000000000000000000403');
INSERT INTO metadata_display_label ( default_locale, id, site_master, key_name) VALUES ('Calculates a value as a result of an expression and stores the results into the database', '8q8kf4e27hs6u2wm9llbnfw5dechwh1wNM200904120000000000000000000030', 'www.runwaysdk.com', '8q8kf4e27hs6u2wm9llbnfw5dechwh1wNM200904120000000000000000000030');
INSERT INTO md_attribute_boolean ( negative_display_label, default_value, positive_display_label, id) VALUES ('qntlqfr84a8cexl2fasiyxvl8sj3czsqNM200904120000000000000000000030', 0, 'zjyk3zfcynantyza2wk3piwizq0q59i5NM200904120000000000000000000030', 'i6j9rzj20ieehcglq72604ze87yembfd00000000000000000000000000000194');
INSERT INTO md_attribute_primitive ( id) VALUES ('i6j9rzj20ieehcglq72604ze87yembfd00000000000000000000000000000194');
INSERT INTO md_attribute_concrete ( column_name, defining_md_class, setter_visibility, setter_visibility_c, immutable, index_name, generate_accessor, system, required, display_label, getter_visibility, getter_visibility_c, index_type, index_type_c, attribute_name, id) VALUES ('is_expression', '0000000000000000000000000000017400000000000000000000000000000001', 'xgm7mb0enjtvulez3he043ywyybiagvp', '20071109NM000000000000000000000520071109NM0000000000000000000001', 0, 'a7mg8oy67ifjjyllcr91gdz7rut7fl', 1, 0, 1, '6ybo6hv9wpni79tbowb8r1dpcnsasxmrNM200904120000000000000000000030', 'r7o8p1i49g3p9388po6hgbsgwf50cyqf', '20071109NM000000000000000000000520071109NM0000000000000000000001', 'sbzg7gbnlap3r0njwl8fuap2dupt3mns', '0000000000000000000000000000040100000000000000000000000000000403', 'isExpression', 'i6j9rzj20ieehcglq72604ze87yembfd00000000000000000000000000000194');
INSERT INTO md_attribute ( id) VALUES ('i6j9rzj20ieehcglq72604ze87yembfd00000000000000000000000000000194');
INSERT INTO metadata ( description, created_by, owner, id, create_date, seq, type, remove, site_master, locked_by, entity_domain, last_updated_by, last_update_date, key_name) VALUES ('8q8kf4e27hs6u2wm9llbnfw5dechwh1wNM200904120000000000000000000030', '0000000000000000000000000000001000000000000000000000000000000003', '0000000000000000000000000000001000000000000000000000000000000003', 'i6j9rzj20ieehcglq72604ze87yembfd00000000000000000000000000000194', '2014-12-05 23:40:28', 4780, 'com.runwaysdk.system.metadata.MdAttributeBoolean', 0, 'www.runwaysdk.com', NULL, NULL, '0000000000000000000000000000001000000000000000000000000000000003', '2014-12-05 23:40:28', 'com.runwaysdk.system.metadata.MdAttributePrimitive.isExpression');
ALTER TABLE md_attribute_primitive ADD COLUMN is_expression  int;
INSERT INTO class_attribute_concrete ( id, parent_id, child_id) VALUES ('i6j9rzj20ieehcglq72604ze87yembfd00000000000000000000000000000073', '0000000000000000000000000000017400000000000000000000000000000001', 'i6j9rzj20ieehcglq72604ze87yembfd00000000000000000000000000000194');
INSERT INTO class_attribute ( id, parent_id, child_id) VALUES ('i6j9rzj20ieehcglq72604ze87yembfd00000000000000000000000000000073', '0000000000000000000000000000017400000000000000000000000000000001', 'i6j9rzj20ieehcglq72604ze87yembfd00000000000000000000000000000194');
INSERT INTO metadata_relationship ( entity_domain, site_master, last_updated_by, id, owner, locked_by, create_date, type, created_by, seq, key_name, last_update_date, parent_id, child_id) VALUES (NULL, 'www.runwaysdk.com', '0000000000000000000000000000001000000000000000000000000000000003', 'i6j9rzj20ieehcglq72604ze87yembfd00000000000000000000000000000073', '0000000000000000000000000000001000000000000000000000000000000003', NULL, '2014-12-05 23:40:28', 'com.runwaysdk.system.metadata.ClassAttributeConcrete', '0000000000000000000000000000001000000000000000000000000000000003', 4781, 'com.runwaysdk.system.metadata.MdAttributePrimitive.isExpression', '2014-12-05 23:40:28', '0000000000000000000000000000017400000000000000000000000000000001', 'i6j9rzj20ieehcglq72604ze87yembfd00000000000000000000000000000194');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('0fte18wx4u3qoum73j766vlov0w9t641', '20071109NM000000000000000000000520071109NM0000000000000000000001');
INSERT INTO metadata_display_label ( default_locale, id, site_master, key_name) VALUES ('Expression', '8mpzlrzjr4t9ucxtdu8hin09ukl5t012NM200904120000000000000000000030', 'www.runwaysdk.com', '8mpzlrzjr4t9ucxtdu8hin09ukl5t012NM200904120000000000000000000030');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('svs5unc17grn2a1fr5okd8zbe9yjacjj', '20071109NM000000000000000000000520071109NM0000000000000000000001');
INSERT INTO md_attribute_indicies (set_id, item_id)  VALUES  ('xt586mpttqfawnvues2m7xew2h18dsei', '0000000000000000000000000000040100000000000000000000000000000403');
INSERT INTO metadata_display_label ( default_locale, id, site_master, key_name) VALUES ('Field for defining an expression that is evaluated on object apply and stored in the database', '6td9jhegm53gozcwtjq1qcf2430h2pzmNM200904120000000000000000000030', 'www.runwaysdk.com', '6td9jhegm53gozcwtjq1qcf2430h2pzmNM200904120000000000000000000030');
INSERT INTO md_attribute_text ( default_value, id) VALUES (NULL, 'i9qolynki2buxjziq31h35c630muacvs00000000000000000000000000000139');
INSERT INTO md_attribute_char ( id) VALUES ('i9qolynki2buxjziq31h35c630muacvs00000000000000000000000000000139');
INSERT INTO md_attribute_primitive ( is_expression, id) VALUES (0, 'i9qolynki2buxjziq31h35c630muacvs00000000000000000000000000000139');
INSERT INTO md_attribute_concrete ( column_name, defining_md_class, setter_visibility, setter_visibility_c, immutable, index_name, generate_accessor, system, required, display_label, getter_visibility, getter_visibility_c, index_type, index_type_c, attribute_name, id) VALUES ('expression', '0000000000000000000000000000017400000000000000000000000000000001', '0fte18wx4u3qoum73j766vlov0w9t641', '20071109NM000000000000000000000520071109NM0000000000000000000001', 0, 'abhzl0y9xq0wed3cre8fbxmw14w8g1', 1, 0, 0, '8mpzlrzjr4t9ucxtdu8hin09ukl5t012NM200904120000000000000000000030', 'svs5unc17grn2a1fr5okd8zbe9yjacjj', '20071109NM000000000000000000000520071109NM0000000000000000000001', 'xt586mpttqfawnvues2m7xew2h18dsei', '0000000000000000000000000000040100000000000000000000000000000403', 'expression', 'i9qolynki2buxjziq31h35c630muacvs00000000000000000000000000000139');
INSERT INTO md_attribute ( id) VALUES ('i9qolynki2buxjziq31h35c630muacvs00000000000000000000000000000139');
INSERT INTO metadata ( description, created_by, owner, id, create_date, seq, type, remove, site_master, locked_by, entity_domain, last_updated_by, last_update_date, key_name) VALUES ('6td9jhegm53gozcwtjq1qcf2430h2pzmNM200904120000000000000000000030', '0000000000000000000000000000001000000000000000000000000000000003', '0000000000000000000000000000001000000000000000000000000000000003', 'i9qolynki2buxjziq31h35c630muacvs00000000000000000000000000000139', '2014-12-05 23:40:28', 4782, 'com.runwaysdk.system.metadata.MdAttributeText', 0, 'www.runwaysdk.com', NULL, NULL, '0000000000000000000000000000001000000000000000000000000000000003', '2014-12-05 23:40:28', 'com.runwaysdk.system.metadata.MdAttributePrimitive.expression');
ALTER TABLE md_attribute_primitive ADD COLUMN expression  text;
INSERT INTO class_attribute_concrete ( id, parent_id, child_id) VALUES ('i9qolynki2buxjziq31h35c630muacvs00000000000000000000000000000073', '0000000000000000000000000000017400000000000000000000000000000001', 'i9qolynki2buxjziq31h35c630muacvs00000000000000000000000000000139');
INSERT INTO class_attribute ( id, parent_id, child_id) VALUES ('i9qolynki2buxjziq31h35c630muacvs00000000000000000000000000000073', '0000000000000000000000000000017400000000000000000000000000000001', 'i9qolynki2buxjziq31h35c630muacvs00000000000000000000000000000139');
INSERT INTO metadata_relationship ( entity_domain, site_master, last_updated_by, id, owner, locked_by, create_date, type, created_by, seq, key_name, last_update_date, parent_id, child_id) VALUES (NULL, 'www.runwaysdk.com', '0000000000000000000000000000001000000000000000000000000000000003', 'i9qolynki2buxjziq31h35c630muacvs00000000000000000000000000000073', '0000000000000000000000000000001000000000000000000000000000000003', NULL, '2014-12-05 23:40:28', 'com.runwaysdk.system.metadata.ClassAttributeConcrete', '0000000000000000000000000000001000000000000000000000000000000003', 4783, 'com.runwaysdk.system.metadata.MdAttributePrimitive.expression', '2014-12-05 23:40:28', '0000000000000000000000000000017400000000000000000000000000000001', 'i9qolynki2buxjziq31h35c630muacvs00000000000000000000000000000139');
UPDATE metadata SET seq= 4784  WHERE id='0000000000000000000000000000017400000000000000000000000000000001';
UPDATE md_attribute_primitive SET is_expression = 0;
INSERT INTO metadata_display_label ( default_locale, id, site_master, key_name) VALUES ('false', 'qyty6dw8fffynu4ht6q5ullqjj7jsasnNM200904120000000000000000000030', 'www.runwaysdk.com', 'qyty6dw8fffynu4ht6q5ullqjj7jsasnNM200904120000000000000000000030');
INSERT INTO metadata_display_label ( default_locale, id, site_master, key_name) VALUES ('true', 'z9rxuciom20hcifbq9eln5pfn95bbaljNM200904120000000000000000000030', 'www.runwaysdk.com', 'z9rxuciom20hcifbq9eln5pfn95bbaljNM200904120000000000000000000030');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('006h5i3rzu4534kdddxcvlmt89h3y8az', '20071109NM000000000000000000000520071109NM0000000000000000000001');
INSERT INTO metadata_display_label ( default_locale, id, site_master, key_name) VALUES ('Is Expression Attribute', 'xrconhggcirzhh4m56c4oeq6ofy1rlipNM200904120000000000000000000030', 'www.runwaysdk.com', 'xrconhggcirzhh4m56c4oeq6ofy1rlipNM200904120000000000000000000030');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('z63jm4cyxp991b7sd3h72gzqpog24p0j', '20071109NM000000000000000000000520071109NM0000000000000000000001');
INSERT INTO md_attribute_indicies (set_id, item_id)  VALUES  ('z8utoudz4lb7s4aepjhm2cmxfi8xzzmq', '0000000000000000000000000000040100000000000000000000000000000403');
INSERT INTO metadata_display_label ( default_locale, id, site_master, key_name) VALUES ('Calculates a value as a result of an expression and stores the results into the database', '71kh84bdcrf3e88pa24i1xykmm1i4dd9NM200904120000000000000000000030', 'www.runwaysdk.com', '71kh84bdcrf3e88pa24i1xykmm1i4dd9NM200904120000000000000000000030');
INSERT INTO md_attribute_boolean ( negative_display_label, default_value, positive_display_label, id) VALUES ('qyty6dw8fffynu4ht6q5ullqjj7jsasnNM200904120000000000000000000030', 0, 'z9rxuciom20hcifbq9eln5pfn95bbaljNM200904120000000000000000000030', 'ipw4nhorictnmmdftvcbgezagg7up73300000000000000000000000000000194');
INSERT INTO md_attribute_primitive ( expression, is_expression, id) VALUES (NULL, 0, 'ipw4nhorictnmmdftvcbgezagg7up73300000000000000000000000000000194');
INSERT INTO md_attribute_concrete ( column_name, defining_md_class, setter_visibility, setter_visibility_c, immutable, index_name, generate_accessor, system, required, display_label, getter_visibility, getter_visibility_c, index_type, index_type_c, attribute_name, id) VALUES ('is_expression', '94dp3weikqddxwpddry9j5ot6my91th100000000000000000000000000000001', '006h5i3rzu4534kdddxcvlmt89h3y8az', '20071109NM000000000000000000000520071109NM0000000000000000000001', 0, 'aqcs4k3hd1a9er3h29g1dmblh5ik1r', 1, 0, 1, 'xrconhggcirzhh4m56c4oeq6ofy1rlipNM200904120000000000000000000030', 'z63jm4cyxp991b7sd3h72gzqpog24p0j', '20071109NM000000000000000000000520071109NM0000000000000000000001', 'z8utoudz4lb7s4aepjhm2cmxfi8xzzmq', '0000000000000000000000000000040100000000000000000000000000000403', 'isExpression', 'ipw4nhorictnmmdftvcbgezagg7up73300000000000000000000000000000194');
INSERT INTO md_attribute ( id) VALUES ('ipw4nhorictnmmdftvcbgezagg7up73300000000000000000000000000000194');
INSERT INTO metadata ( description, created_by, owner, id, create_date, seq, type, remove, site_master, locked_by, entity_domain, last_updated_by, last_update_date, key_name) VALUES ('71kh84bdcrf3e88pa24i1xykmm1i4dd9NM200904120000000000000000000030', '0000000000000000000000000000001000000000000000000000000000000003', '0000000000000000000000000000001000000000000000000000000000000003', 'ipw4nhorictnmmdftvcbgezagg7up73300000000000000000000000000000194', '2014-12-12 14:38:00', 5800, 'com.runwaysdk.system.metadata.MdAttributeBoolean', 0, 'www.runwaysdk.com', NULL, NULL, '0000000000000000000000000000001000000000000000000000000000000003', '2014-12-12 14:38:00', 'com.runwaysdk.system.metadata.MdWebPrimitive.isExpression');
ALTER TABLE md_web_primitive ADD COLUMN is_expression  int;
INSERT INTO class_attribute_concrete ( id, parent_id, child_id) VALUES ('ipw4nhorictnmmdftvcbgezagg7up73300000000000000000000000000000073', '94dp3weikqddxwpddry9j5ot6my91th100000000000000000000000000000001', 'ipw4nhorictnmmdftvcbgezagg7up73300000000000000000000000000000194');
INSERT INTO class_attribute ( id, parent_id, child_id) VALUES ('ipw4nhorictnmmdftvcbgezagg7up73300000000000000000000000000000073', '94dp3weikqddxwpddry9j5ot6my91th100000000000000000000000000000001', 'ipw4nhorictnmmdftvcbgezagg7up73300000000000000000000000000000194');
INSERT INTO metadata_relationship ( entity_domain, site_master, last_updated_by, id, owner, locked_by, create_date, type, created_by, seq, key_name, last_update_date, parent_id, child_id) VALUES (NULL, 'www.runwaysdk.com', '0000000000000000000000000000001000000000000000000000000000000003', 'ipw4nhorictnmmdftvcbgezagg7up73300000000000000000000000000000073', '0000000000000000000000000000001000000000000000000000000000000003', NULL, '2014-12-12 14:38:00', 'com.runwaysdk.system.metadata.ClassAttributeConcrete', '0000000000000000000000000000001000000000000000000000000000000003', 5801, 'com.runwaysdk.system.metadata.MdWebPrimitive.isExpression', '2014-12-12 14:38:00', '94dp3weikqddxwpddry9j5ot6my91th100000000000000000000000000000001', 'ipw4nhorictnmmdftvcbgezagg7up73300000000000000000000000000000194');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('92odjz1d47u3j4gjt09cypxnvg0i7zr9', '20071109NM000000000000000000000520071109NM0000000000000000000001');
INSERT INTO metadata_display_label ( default_locale, id, site_master, key_name) VALUES ('Expression', 'zovxfky1cttrg58m2oolejzirux6pus5NM200904120000000000000000000030', 'www.runwaysdk.com', 'zovxfky1cttrg58m2oolejzirux6pus5NM200904120000000000000000000030');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('sx260miyj18vj7zdphjhl4t2gj25q7bg', '20071109NM000000000000000000000520071109NM0000000000000000000001');
INSERT INTO md_attribute_indicies (set_id, item_id)  VALUES  ('smzjyzpmbpzm4qe0ra1501ni1i2ffmeb', '0000000000000000000000000000040100000000000000000000000000000403');
INSERT INTO metadata_display_label ( default_locale, id, site_master, key_name) VALUES ('Field for defining an expression that is evaluated on object apply and stored in the database', '0c828q4pmsesckx9dak93r8vu24wszokNM200904120000000000000000000030', 'www.runwaysdk.com', '0c828q4pmsesckx9dak93r8vu24wszokNM200904120000000000000000000030');
INSERT INTO md_attribute_text ( default_value, id) VALUES (NULL, 'igvrol3trqk0lxjauzb0vuhpoxpcp0eb00000000000000000000000000000139');
INSERT INTO md_attribute_char ( id) VALUES ('igvrol3trqk0lxjauzb0vuhpoxpcp0eb00000000000000000000000000000139');
INSERT INTO md_attribute_primitive ( expression, is_expression, id) VALUES (NULL, 0, 'igvrol3trqk0lxjauzb0vuhpoxpcp0eb00000000000000000000000000000139');
INSERT INTO md_attribute_concrete ( column_name, defining_md_class, setter_visibility, setter_visibility_c, immutable, index_name, generate_accessor, system, required, display_label, getter_visibility, getter_visibility_c, index_type, index_type_c, attribute_name, id) VALUES ('expression', '94dp3weikqddxwpddry9j5ot6my91th100000000000000000000000000000001', '92odjz1d47u3j4gjt09cypxnvg0i7zr9', '20071109NM000000000000000000000520071109NM0000000000000000000001', 0, 'ak2baenrbueprpmt9e9kx47okzy9ph', 1, 0, 0, 'zovxfky1cttrg58m2oolejzirux6pus5NM200904120000000000000000000030', 'sx260miyj18vj7zdphjhl4t2gj25q7bg', '20071109NM000000000000000000000520071109NM0000000000000000000001', 'smzjyzpmbpzm4qe0ra1501ni1i2ffmeb', '0000000000000000000000000000040100000000000000000000000000000403', 'expression', 'igvrol3trqk0lxjauzb0vuhpoxpcp0eb00000000000000000000000000000139');
INSERT INTO md_attribute ( id) VALUES ('igvrol3trqk0lxjauzb0vuhpoxpcp0eb00000000000000000000000000000139');
INSERT INTO metadata ( description, created_by, owner, id, create_date, seq, type, remove, site_master, locked_by, entity_domain, last_updated_by, last_update_date, key_name) VALUES ('0c828q4pmsesckx9dak93r8vu24wszokNM200904120000000000000000000030', '0000000000000000000000000000001000000000000000000000000000000003', '0000000000000000000000000000001000000000000000000000000000000003', 'igvrol3trqk0lxjauzb0vuhpoxpcp0eb00000000000000000000000000000139', '2014-12-12 14:38:00', 5802, 'com.runwaysdk.system.metadata.MdAttributeText', 0, 'www.runwaysdk.com', NULL, NULL, '0000000000000000000000000000001000000000000000000000000000000003', '2014-12-12 14:38:00', 'com.runwaysdk.system.metadata.MdWebPrimitive.expression');
ALTER TABLE md_web_primitive ADD COLUMN expression  text;
INSERT INTO class_attribute_concrete ( id, parent_id, child_id) VALUES ('igvrol3trqk0lxjauzb0vuhpoxpcp0eb00000000000000000000000000000073', '94dp3weikqddxwpddry9j5ot6my91th100000000000000000000000000000001', 'igvrol3trqk0lxjauzb0vuhpoxpcp0eb00000000000000000000000000000139');
INSERT INTO class_attribute ( id, parent_id, child_id) VALUES ('igvrol3trqk0lxjauzb0vuhpoxpcp0eb00000000000000000000000000000073', '94dp3weikqddxwpddry9j5ot6my91th100000000000000000000000000000001', 'igvrol3trqk0lxjauzb0vuhpoxpcp0eb00000000000000000000000000000139');
INSERT INTO metadata_relationship ( entity_domain, site_master, last_updated_by, id, owner, locked_by, create_date, type, created_by, seq, key_name, last_update_date, parent_id, child_id) VALUES (NULL, 'www.runwaysdk.com', '0000000000000000000000000000001000000000000000000000000000000003', 'igvrol3trqk0lxjauzb0vuhpoxpcp0eb00000000000000000000000000000073', '0000000000000000000000000000001000000000000000000000000000000003', NULL, '2014-12-12 14:38:00', 'com.runwaysdk.system.metadata.ClassAttributeConcrete', '0000000000000000000000000000001000000000000000000000000000000003', 5803, 'com.runwaysdk.system.metadata.MdWebPrimitive.expression', '2014-12-12 14:38:00', '94dp3weikqddxwpddry9j5ot6my91th100000000000000000000000000000001', 'igvrol3trqk0lxjauzb0vuhpoxpcp0eb00000000000000000000000000000139');
UPDATE metadata SET seq= 5804  WHERE id='94dp3weikqddxwpddry9j5ot6my91th100000000000000000000000000000001';
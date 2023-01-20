UPDATE metadata SET type='com.runwaysdk.system.metadata.graph.MdEmbeddedGraphClass' WHERE key_name='com.runwaysdk.graph.EmbeddedLocalValue';
INSERT INTO md_embedded_graph_class SELECT oid FROM metadata where key_name='com.runwaysdk.graph.EmbeddedLocalValue';
DELETE FROM md_vertex WHERE oid=(select oid from metadata where key_name='com.runwaysdk.graph.EmbeddedLocalValue');

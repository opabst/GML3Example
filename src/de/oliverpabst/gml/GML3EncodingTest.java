package de.oliverpabst.gml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.namespace.QName;
import javax.xml.transform.TransformerException;

import org.eclipse.xsd.XSDSchema;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.gml3.v3_2.GMLConfiguration;
import org.geotools.wfs.v1_0.WFS;
import org.geotools.xml.Configuration;
import org.geotools.xml.Encoder;
import org.opengis.geometry.primitive.Point;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

import net.opengis.wfs.FeatureCollectionType;
import net.opengis.wfs.WfsFactory;

public class GML3EncodingTest {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		buildFeatureCollection();
		encodeGML3();
		//encodeWFS10();
	}
	
	public static void encodeGML3() {
		FeatureCollectionType fc = buildFeatureCollection();
		Configuration config = new GMLConfiguration();
		// TODO: Set up schema
		XSDSchema schema = null;
		Encoder encoder = new Encoder(config, schema);
		ByteArrayOutputStream xml = new ByteArrayOutputStream();
		// TODO: Define custom feature collection for given type
		QName coll = new QName("http://www.dbs.uni-hannover.de/test", "TestCollection");
		try {
			encoder.encode(fc, coll, xml);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(xml);
		try {
			xml.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void encodeWFS10() {
		FeatureCollectionType fc = buildFeatureCollection();
		Encoder e = new Encoder( new org.geotools.wfs.v1_0.WFSConfiguration() );
        e.getNamespaces().declarePrefix( "geotools", "http://geotools.org");
        e.setIndenting(true);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
			e.encode(fc, WFS.FeatureCollection, out);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
        System.out.println(out.toString());
        try {
			out.close();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
        Document d = null;
       /* try {
			d = e.encodeAsDOM( fc, WFS.FeatureCollection );
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SAXException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (TransformerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
        //NodeList nl = d.getElementsByTagName( "gml:Point" );
	}
	
	public static FeatureCollectionType buildFeatureCollection() {
		FeatureCollectionType fc = WfsFactory.eINSTANCE.createFeatureCollectionType();
        FeatureCollection features = new DefaultFeatureCollection(null,null);
        
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName( "feature" );
        tb.setNamespaceURI( "http://geotools.org");
        tb.add( "geometry", Point.class );
        tb.add( "integer", Integer.class);
        
        SimpleFeatureBuilder b = new SimpleFeatureBuilder( tb.buildFeatureType() );
        b.add( new GeometryFactory().createPoint( new Coordinate( 0, 0 ) ) );
        b.add( 0 );
        features.add( b.buildFeature( "zero" ) );
        
        b.add( new GeometryFactory().createPoint( new Coordinate( 1, 1 ) ) );
        b.add( 1 );
        features.add( b.buildFeature( "one" ) );
        
        fc.getFeature().add( features );
        
        return fc;
	}

}

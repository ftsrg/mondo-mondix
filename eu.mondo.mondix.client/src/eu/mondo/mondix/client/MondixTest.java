package eu.mondo.mondix.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;

import eu.mondo.mondix.core.IMondixInstance;
import eu.mondo.mondix.core.IMondixRelation;
import eu.mondo.mondix.core.INullaryQueryInstance;
import eu.mondo.mondix.core.IQueryInstance;
import eu.mondo.mondix.core.IUnaryQueryInstance;
import eu.mondo.mondix.implementation.hashmap.ImmutableMapRow;
import eu.mondo.mondix.implementation.hashmap.database.Database;
import eu.mondo.mondix.implementation.hashmap.live.ChangeAwareMondixInstance;
import eu.mondo.mondix.live.IChangeAwareMondixRelation;
import eu.mondo.mondix.live.ILiveQueryInstance;

public class MondixTest {
	
	protected Database db;
	protected IMondixInstance mondixInstance;
	protected IMondixRelation ageRelation;
	
	@Before
	public void setUp() throws Exception {
		// create new database
		db = new Database();
		
		// create new relations
		HashSet<ImmutableMapRow> ages = new HashSet<ImmutableMapRow>();
		ImmutableMapRow age1 = new ImmutableMapRow(ImmutableMap.<String, Object>builder()
			    .put("name", "John").put("year", 26).build());
		ages.add(age1);
		ImmutableMapRow age2 = new ImmutableMapRow(ImmutableMap.<String, Object>builder()
			    .put("name", "Jill").put("year", 23).build());
		ages.add(age2);
		ImmutableMapRow age3 = new ImmutableMapRow(ImmutableMap.<String, Object>builder()
			    .put("name", "Jack").put("year", 26).build());
		ages.add(age3);
		db.addRelation("age", ages);
		
		mondixInstance = db.getMondixInstance();
		
    }
	
	@Test
	public void testPublishedRelations() {
		IUnaryQueryInstance publishedRelations = mondixInstance.getPublishedRelationNames();
		StringBuilder sb = new StringBuilder();
		for( Object value : publishedRelations.getValues()) {
			sb.append(value);
			sb.append('\n');
		}
		System.out.println(sb.toString());
		assertEquals("\nage\n", sb.toString());
	}

	@Test
	public void testCatalogRelation() {
		ageRelation = mondixInstance.getCatalogRelation();
		
		int arity = ageRelation.getArity();
		System.out.println(arity);
		assertEquals(1, arity);
		
		IQueryInstance queryInstance = ageRelation.openQueryInstance();
		int coutOfTuples = queryInstance.getCountOfTuples();
		System.out.println(coutOfTuples);
		assertEquals(2, coutOfTuples);
		
		StringBuilder sb = new StringBuilder();
		for(List<?> tuple : queryInstance.getAllTuples()) {
			sb.append(tupleToString(tuple));
		}
		System.out.println(sb.toString());
		assertTrue(sb.toString().contains("\n  \n"));
		assertTrue(sb.toString().contains("\n  age\n"));
	}

	@Test
	public void testAgeRelation() {
		ageRelation = mondixInstance.getBaseRelationByName("age");
		
		int arity = ageRelation.getArity();
		System.out.println(arity);
		assertEquals(2, arity);
		
		IQueryInstance queryInstance = ageRelation.openQueryInstance();
		int coutOfTuples = queryInstance.getCountOfTuples();
		System.out.println(coutOfTuples);
		assertEquals(3, coutOfTuples);
		
		StringBuilder sb = new StringBuilder();
		for(List<?> tuple : queryInstance.getAllTuples()) {
			sb.append(tupleToString(tuple));
		}
		System.out.println(sb.toString());
		assertTrue(sb.toString().contains("\n  Jill\n  23\n"));
		assertTrue(sb.toString().contains("\n  John\n  26\n"));
		assertTrue(sb.toString().contains("\n  Jack\n  26\n"));
		

		ArrayList<String> selectedColumnNames = new ArrayList<String>();
		selectedColumnNames.add("name");
		selectedColumnNames.add("year");
		HashMap<String, Object> filter = new HashMap<String, Object>();
		filter.put("name", null);
		filter.put("year", 26);
		IQueryInstance queryInstanceFiltered = ageRelation.openQueryInstance(selectedColumnNames, filter);
		int coutOfTuplesFiltered = queryInstanceFiltered.getCountOfTuples();
		System.out.println(coutOfTuplesFiltered);
		assertEquals(2, coutOfTuplesFiltered);
		
		StringBuilder sbFiltered = new StringBuilder();
		for(List<?> tuple : queryInstanceFiltered.getAllTuples()) {
			sbFiltered.append(tupleToString(tuple));
		}
		System.out.println(sbFiltered.toString());
		assertFalse(sbFiltered.toString().contains("\n  Jill\n  23\n"));
		assertFalse(sbFiltered.toString().contains("\n  Jill\n  23\n"));
		assertTrue(sbFiltered.toString().contains("\n  John\n  26\n"));
		assertTrue(sbFiltered.toString().contains("\n  Jack\n  26\n"));
	}
	
	private String tupleToString(List<?> tuple) {
		StringBuilder sb = new StringBuilder();
		sb.append("Tuple\n");
		for(Object value : tuple) {
			sb.append("  ").append(value.toString()).append("\n");
		}
		return sb.toString();
	}
	

	@Test
	public void testUnary() throws Exception {
		// create new database
		Database bikeDB = new Database();
		
		// create new relations
		HashSet<ImmutableMapRow> bikes = new HashSet<ImmutableMapRow>();
		ImmutableMapRow bike1 = new ImmutableMapRow(ImmutableMap.<String, Object>builder()
			    .put("name", "Scott").build());
		bikes.add(bike1);
		ImmutableMapRow bike2 = new ImmutableMapRow(ImmutableMap.<String, Object>builder()
			    .put("name", "Specialized").build());
		bikes.add(bike2);
		ImmutableMapRow bike3 = new ImmutableMapRow(ImmutableMap.<String, Object>builder()
			    .put("name", "Gepida").build());
		bikes.add(bike3);
		bikeDB.addRelation("bike", bikes);
		
		IMondixInstance mondixInstance = bikeDB.getMondixInstance();
		
		StringBuilder sb = new StringBuilder();
		IMondixRelation unaryBikeRelation = mondixInstance.getBaseRelationByName("bike");
		IUnaryQueryInstance unaryBikeQueryInstance = (IUnaryQueryInstance) unaryBikeRelation.openQueryInstance();
		for(Object value : unaryBikeQueryInstance.getValues()) {
			sb.append(value.toString()).append('\n');
		}
		System.out.println(sb);
		assertEquals("Gepida\nScott\nSpecialized\n", sb.toString());
    }

	@Test
	public void testNullary() throws Exception {
		// create new database
		Database unaryDB = new Database();
		
		// create new relations
		HashSet<ImmutableMapRow> unaryTuples = new HashSet<ImmutableMapRow>();
		ImmutableMapRow emptyTuple = new ImmutableMapRow(ImmutableMap.<String, Object>builder().build());
		unaryTuples.add(emptyTuple);
		unaryDB.addRelation("exists", unaryTuples);
		
		IMondixInstance mondixInstance = unaryDB.getMondixInstance();
		
		IMondixRelation nullaryExistsRelation = mondixInstance.getBaseRelationByName("exists");
		INullaryQueryInstance nullaryExistsQueryInstance = (INullaryQueryInstance) nullaryExistsRelation.openQueryInstance();
		System.out.println(nullaryExistsQueryInstance.isTrue());
		assertTrue(nullaryExistsQueryInstance.isTrue());
    }

	@Test
	public void live() throws Exception {
		// create new database
		db = new Database();
		
		// create new relations
		HashSet<ImmutableMapRow> ages = new HashSet<ImmutableMapRow>();
		ImmutableMapRow age1 = new ImmutableMapRow(ImmutableMap.<String, Object>builder()
			    .put("name", "John").put("year", 26).build());
		ages.add(age1);
		ImmutableMapRow age2 = new ImmutableMapRow(ImmutableMap.<String, Object>builder()
			    .put("name", "Jill").put("year", 23).build());
		ages.add(age2);
		ImmutableMapRow age3 = new ImmutableMapRow(ImmutableMap.<String, Object>builder()
			    .put("name", "Jack").put("year", 26).build());
		ages.add(age3);
		db.addRelation("age", ages);
		
		ChangeAwareMondixInstance<ImmutableMapRow> changeAwareMondixInstance = db.getChangeAwareMondixInstance();
		IChangeAwareMondixRelation liveAgeRelation = changeAwareMondixInstance.getBaseRelationByName("age");
		ILiveQueryInstance liveQueryInstance = liveAgeRelation.openQueryInstance();
		
		int coutOfTuples = liveQueryInstance.getCountOfTuples();
		System.out.println(coutOfTuples);
		assertEquals(3, coutOfTuples);
		
		// basic live query is initialized
		StringBuilder sb = new StringBuilder();
		for(List<?> tuple : liveQueryInstance.getAllTuples()) {
			sb.append(tupleToString(tuple));
		}
		System.out.println(sb.toString());
		assertTrue(sb.toString().contains("\n  Jill\n  23\n"));
		assertTrue(sb.toString().contains("\n  John\n  26\n"));
		assertTrue(sb.toString().contains("\n  Jack\n  26\n"));
		
		// test live data is added
		ImmutableMapRow age4 = new ImmutableMapRow(ImmutableMap.<String, Object>builder()
			    .put("name", "Jenny").put("year", 26).build());
		changeAwareMondixInstance.addRow("age", age4);
		
		int coutOfTuples2 = liveQueryInstance.getCountOfTuples();
		System.out.println(coutOfTuples2);
		assertEquals(4, coutOfTuples2);
		
		// test results are filtered
		ArrayList<String> selectedColumnNames = new ArrayList<String>();
		selectedColumnNames.add("name");
		selectedColumnNames.add("year");
		HashMap<String, Object> filter = new HashMap<String, Object>();
		filter.put("name", null);
		filter.put("year", 26);
		ILiveQueryInstance liveQueryInstanceFiltered = liveAgeRelation.openQueryInstance(selectedColumnNames, filter);
		StringBuilder sbFiltered = new StringBuilder();
		for(List<?> tuple : liveQueryInstanceFiltered.getAllTuples()) {
			sbFiltered.append(tupleToString(tuple));
		}
		System.out.println(sbFiltered.toString());
		assertFalse(sbFiltered.toString().contains("\n  Jill\n  23\n"));
		assertTrue(sbFiltered.toString().contains("\n  Jenny\n  26\n"));
		assertTrue(sbFiltered.toString().contains("\n  John\n  26\n"));
		assertTrue(sbFiltered.toString().contains("\n  Jack\n  26\n"));
		
		// add matching, must show up as result
		ImmutableMapRow age5 = new ImmutableMapRow(ImmutableMap.<String, Object>builder()
			    .put("name", "Johanna").put("year", 26).build());
		changeAwareMondixInstance.addRow("age", age5);
		StringBuilder sbFiltered2 = new StringBuilder();
		for(List<?> tuple : liveQueryInstanceFiltered.getAllTuples()) {
			sbFiltered2.append(tupleToString(tuple));
		}
		System.out.println(sbFiltered2.toString());
		assertFalse(sbFiltered2.toString().contains("\n  Jill\n  23\n"));
		assertTrue(sbFiltered2.toString().contains("\n  Jenny\n  26\n"));
		assertTrue(sbFiltered2.toString().contains("\n  Johanna\n  26\n"));

		// add filtered, does not show up as result
		ImmutableMapRow age6 = new ImmutableMapRow(ImmutableMap.<String, Object>builder()
			    .put("name", "Joe").put("year", 35).build());
		changeAwareMondixInstance.addRow("age", age6);
		StringBuilder sbFiltered3 = new StringBuilder();
		for(List<?> tuple : liveQueryInstanceFiltered.getAllTuples()) {
			sbFiltered3.append(tupleToString(tuple));
		}
		System.out.println(sbFiltered3.toString());
		assertFalse(sbFiltered3.toString().contains("\n  Joe\n  35\n"));
		assertFalse(sbFiltered3.toString().contains("\n  Jill\n  23\n"));
		assertTrue(sbFiltered3.toString().contains("\n  Jenny\n  26\n"));
		assertTrue(sbFiltered3.toString().contains("\n  Johanna\n  26\n"));

		// but the "filtered" data must show up as a result of a non-filtered query result in the same relation
		StringBuilder sbNotFiltered4 = new StringBuilder();
		for(List<?> tuple : liveQueryInstance.getAllTuples()) {
			sbNotFiltered4.append(tupleToString(tuple));
		}
		System.out.println(sbNotFiltered4.toString());
		assertTrue(sbNotFiltered4.toString().contains("\n  Joe\n  35\n"));
		assertTrue(sbNotFiltered4.toString().contains("\n  Jill\n  23\n"));
		assertTrue(sbNotFiltered4.toString().contains("\n  Jenny\n  26\n"));
		assertTrue(sbNotFiltered4.toString().contains("\n  Johanna\n  26\n"));
		
		// test getting change callbacks and consistency callbacks
		MyChangeCallback changeCallback = new MyChangeCallback();
		liveQueryInstance.addChangeListener(changeCallback);
		MyConsistencyCallback consistencyCallback = new MyConsistencyCallback();
		changeAwareMondixInstance.addConsistencyListener(consistencyCallback);
		ImmutableMapRow age7 = new ImmutableMapRow(ImmutableMap.<String, Object>builder()
			    .put("name", "Jake").put("year", 29).build());
		changeAwareMondixInstance.addRow("age", age7);
		changeAwareMondixInstance.removeRow("age", age5);
		System.out.println(changeCallback.getNotificationsStr());
		assertTrue(changeCallback.getNotificationsStr().toString().contains("inserted: true; tuple: [Jake, 29]"));
		assertTrue(changeCallback.getNotificationsStr().toString().contains("inserted: false; tuple: [Johanna, 26]"));
		System.out.println(consistencyCallback.getNumberOfCallbacks());
		assertEquals(2, consistencyCallback.getNumberOfCallbacks()); // one after addition, one after deletion
		
		// test whether Johanna is deleted from the filtered results, as the callback says
		StringBuilder sbFiltered4 = new StringBuilder();
		for(List<?> tuple : liveQueryInstanceFiltered.getAllTuples()) {
			sbFiltered4.append(tupleToString(tuple));
		}
		System.out.println(sbFiltered4.toString());
		assertFalse(sbFiltered4.toString().contains("\n  Joe\n  35\n"));
		assertFalse(sbFiltered4.toString().contains("\n  Jill\n  23\n"));
		assertTrue(sbFiltered4.toString().contains("\n  Jenny\n  26\n"));
		assertFalse(sbFiltered4.toString().contains("\n  Johanna\n  26\n"));
		
    }
	
}

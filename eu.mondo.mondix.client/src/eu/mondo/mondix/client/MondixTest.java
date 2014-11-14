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
import eu.mondo.mondix.core.IMondixView;
import eu.mondo.mondix.core.INullaryView;
import eu.mondo.mondix.core.IUnaryView;
import eu.mondo.mondix.implementation.hashmap.database.Database;
import eu.mondo.mondix.live.IChangeAwareMondixInstance;
import eu.mondo.mondix.live.IChangeAwareMondixRelation;
import eu.mondo.mondix.live.ILiveView;

public class MondixTest {
	
	protected Database db;
	protected IMondixInstance mondixInstance;
	protected IMondixRelation ageRelation;
	protected HashSet<ImmutableMap<String, Object>> ages;
	
	@Before
	public void setUp() throws Exception {
		// create new database
		db = new Database();
		
		ages = new HashSet<ImmutableMap<String, Object>>();
		ImmutableMap<String, Object> age1 = ImmutableMap.<String, Object>builder()
			    .put("name", "John").put("year", 26).build();
		ages.add(age1);
		ImmutableMap<String, Object> age2 = ImmutableMap.<String, Object>builder()
			    .put("name", "Jill").put("year", 23).build();
		ages.add(age2);
		ImmutableMap<String, Object> age3 = ImmutableMap.<String, Object>builder()
			    .put("name", "Jack").put("year", 26).build();
		ages.add(age3);
		
		ArrayList<String> columns = new ArrayList<String>();
		columns.add("name");
		columns.add("year");
		db.addRelation("age", ages, columns);
		
		mondixInstance = db.getMondixInstance();
    }
	
	@Test
	public void testPublishedRelations() {
		IUnaryView publishedRelations = mondixInstance.getPublishedRelationNames();
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
		
		IMondixView mondixView = ageRelation.openView();
		int coutOfTuples = mondixView.getCountOfTuples();
		System.out.println(coutOfTuples);
		assertEquals(2, coutOfTuples);
		
		StringBuilder sb = new StringBuilder();
		for(List<?> tuple : mondixView.getAllTuples()) {
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
		
		IMondixView mondixView = ageRelation.openView();
		int coutOfTuples = mondixView.getCountOfTuples();
		System.out.println(coutOfTuples);
		assertEquals(3, coutOfTuples);
		
		StringBuilder sb = new StringBuilder();
		for(List<?> tuple : mondixView.getAllTuples()) {
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
		IMondixView viewFiltered = ageRelation.openView(selectedColumnNames, filter);
		int coutOfTuplesFiltered = viewFiltered.getCountOfTuples();
		System.out.println(coutOfTuplesFiltered);
		assertEquals(2, coutOfTuplesFiltered);
		
		StringBuilder sbFiltered = new StringBuilder();
		for(List<?> tuple : viewFiltered.getAllTuples()) {
			sbFiltered.append(tupleToString(tuple));
		}
		System.out.println(sbFiltered.toString());
		assertFalse(sbFiltered.toString().contains("\n  Jill\n  23\n"));
		assertFalse(sbFiltered.toString().contains("\n  Jill\n  23\n"));
		assertTrue(sbFiltered.toString().contains("\n  John\n  26\n"));
		assertTrue(sbFiltered.toString().contains("\n  Jack\n  26\n"));
		
		// When new data is added to the relation, it does not reflected in the MondixInstance.
		ImmutableMap<String, Object> age4 = ImmutableMap.<String, Object>builder()
			    .put("name", "Jacob").put("year", 29).build();
		ages.add(age4);
		IMondixRelation ageRelation4 = mondixInstance.getBaseRelationByName("age");
		IMondixView view4 = ageRelation4.openView();
		System.out.println(view4.getCountOfTuples());
		assertEquals(3, view4.getCountOfTuples());
	}
	
	private String tupleToString(List<?> tuple) {
		StringBuilder sb = new StringBuilder();
		sb.append("Tuple\n");
		for(Object value : tuple) {
			sb.append("  ").append(value.toString()).append("\n");
		}
		return sb.toString();
	}
	
	private HashSet<ImmutableMap<String, Object>> createBikesRelation() {
		// create new relations
		HashSet<ImmutableMap<String, Object>> bikes = new HashSet<ImmutableMap<String, Object>>();
		ImmutableMap<String, Object> bike1 = ImmutableMap.<String, Object>builder()
			    .put("name", "Scott").build();
		bikes.add(bike1);
		ImmutableMap<String, Object> bike2 = ImmutableMap.<String, Object>builder()
			    .put("name", "Specialized").build();
		bikes.add(bike2);
		ImmutableMap<String, Object> bike3 = ImmutableMap.<String, Object>builder()
			    .put("name", "Gepida").build();
		bikes.add(bike3);
		
		return bikes;
	}
	
	@Test
	public void testUnary() throws Exception {
		// create new database
		Database bikeDB = new Database();
		HashSet<ImmutableMap<String, Object>> bikes = createBikesRelation();
		bikeDB.addRelation("bike", bikes, null);
		
		IMondixInstance mondixInstance = bikeDB.getMondixInstance();
		
		StringBuilder sb = new StringBuilder();
		IMondixRelation unaryBikeRelation = mondixInstance.getBaseRelationByName("bike");
		IUnaryView unaryBikeView = (IUnaryView) unaryBikeRelation.openView();
		for(Object value : unaryBikeView.getValues()) {
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
		HashSet<ImmutableMap<String, Object>> unaryTuples = new HashSet<ImmutableMap<String, Object>>();
		ImmutableMap<String, Object> emptyTuple = ImmutableMap.<String, Object>builder().build();
		unaryTuples.add(emptyTuple);
		unaryDB.addRelation("exists", unaryTuples, null);

		// create new relations
		HashSet<ImmutableMap<String, Object>> unaryTuplesF = new HashSet<ImmutableMap<String, Object>>();
		ArrayList<String> columns = new ArrayList<String>();
		unaryDB.addRelation("notExists", unaryTuplesF, columns);
		
		IMondixInstance mondixInstance = unaryDB.getMondixInstance();
		
		IMondixRelation nullaryExistsRelation = mondixInstance.getBaseRelationByName("exists");
		INullaryView nullaryExistsView = (INullaryView) nullaryExistsRelation.openView();
		System.out.println(nullaryExistsView.isTrue());
		assertTrue(nullaryExistsView.isTrue());
		
		IMondixRelation nullaryExistsRelationF = mondixInstance.getBaseRelationByName("notExists");
		INullaryView nullaryExistsViewF = (INullaryView) nullaryExistsRelationF.openView();
		System.out.println(nullaryExistsViewF.isTrue());
		assertFalse(nullaryExistsViewF.isTrue());
    }
	
	private IChangeAwareMondixInstance setupChangeAwareMI() throws Exception {
		// create new database
		db = new Database();

		// create new relations
		HashSet<ImmutableMap<String, Object>> ages = new HashSet<ImmutableMap<String, Object>>();
		ImmutableMap<String, Object> age1 = ImmutableMap.<String, Object>builder()
				.put("name", "John").put("year", 26).build();
		ages.add(age1);
		ImmutableMap<String, Object> age2 = ImmutableMap.<String, Object>builder()
				.put("name", "Jill").put("year", 23).build();
		ages.add(age2);
		ImmutableMap<String, Object> age3 = ImmutableMap.<String, Object>builder()
				.put("name", "Jack").put("year", 26).build();
		ages.add(age3);
		db.addRelation("age", ages, null);

		IChangeAwareMondixInstance changeAwareMondixInstance = db.getChangeAwareMondixInstance();
		return changeAwareMondixInstance;
	}
	
	@Test
	public void live() throws Exception {
		IChangeAwareMondixInstance changeAwareMondixInstance = setupChangeAwareMI();
		IChangeAwareMondixRelation liveAgeRelation = (IChangeAwareMondixRelation) changeAwareMondixInstance.getBaseRelationByName("age");
		ILiveView liveView = liveAgeRelation.openView();
		
		int coutOfTuples = liveView.getCountOfTuples();
		System.out.println(coutOfTuples);
		assertEquals(3, coutOfTuples);
		
		// basic live query is initialized
		StringBuilder sb = new StringBuilder();
		for(List<?> tuple : liveView.getAllTuples()) {
			sb.append(tupleToString(tuple));
		}
		System.out.println(sb.toString());
		assertTrue(sb.toString().contains("\n  Jill\n  23\n"));
		assertTrue(sb.toString().contains("\n  John\n  26\n"));
		assertTrue(sb.toString().contains("\n  Jack\n  26\n"));
		
		// test live data is added
		ImmutableMap<String, Object> age4 = ImmutableMap.<String, Object>builder()
			    .put("name", "Jenny").put("year", 26).build();
		db.addRow("age", age4);
		
		int coutOfTuples2 = liveView.getCountOfTuples();
		System.out.println(coutOfTuples2);
		assertEquals(4, coutOfTuples2);
		
		// test results are filtered
		ArrayList<String> selectedColumnNames = new ArrayList<String>();
		selectedColumnNames.add("name");
		selectedColumnNames.add("year");
		HashMap<String, Object> filter = new HashMap<String, Object>();
		filter.put("name", null);
		filter.put("year", 26);
		ILiveView liveViewFiltered = liveAgeRelation.openView(selectedColumnNames, filter);
		StringBuilder sbFiltered = new StringBuilder();
		for(List<?> tuple : liveViewFiltered.getAllTuples()) {
			sbFiltered.append(tupleToString(tuple));
		}
		System.out.println(sbFiltered.toString());
		assertFalse(sbFiltered.toString().contains("\n  Jill\n  23\n"));
		assertTrue(sbFiltered.toString().contains("\n  Jenny\n  26\n"));
		assertTrue(sbFiltered.toString().contains("\n  John\n  26\n"));
		assertTrue(sbFiltered.toString().contains("\n  Jack\n  26\n"));
		
		// add matching, must show up as result
		ImmutableMap<String, Object> age5 = ImmutableMap.<String, Object>builder()
			    .put("name", "Johanna").put("year", 26).build();
		db.addRow("age", age5);
		StringBuilder sbFiltered2 = new StringBuilder();
		for(List<?> tuple : liveViewFiltered.getAllTuples()) {
			sbFiltered2.append(tupleToString(tuple));
		}
		System.out.println(sbFiltered2.toString());
		assertFalse(sbFiltered2.toString().contains("\n  Jill\n  23\n"));
		assertTrue(sbFiltered2.toString().contains("\n  Jenny\n  26\n"));
		assertTrue(sbFiltered2.toString().contains("\n  Johanna\n  26\n"));

		// add filtered, does not show up as result
		ImmutableMap<String, Object> age6 = ImmutableMap.<String, Object>builder()
			    .put("name", "Joe").put("year", 35).build();
		db.addRow("age", age6);
		StringBuilder sbFiltered3 = new StringBuilder();
		for(List<?> tuple : liveViewFiltered.getAllTuples()) {
			sbFiltered3.append(tupleToString(tuple));
		}
		System.out.println(sbFiltered3.toString());
		assertFalse(sbFiltered3.toString().contains("\n  Joe\n  35\n"));
		assertFalse(sbFiltered3.toString().contains("\n  Jill\n  23\n"));
		assertTrue(sbFiltered3.toString().contains("\n  Jenny\n  26\n"));
		assertTrue(sbFiltered3.toString().contains("\n  Johanna\n  26\n"));

		// but the "filtered" data must show up as a result of a non-filtered query result in the same relation
		StringBuilder sbNotFiltered4 = new StringBuilder();
		for(List<?> tuple : liveView.getAllTuples()) {
			sbNotFiltered4.append(tupleToString(tuple));
		}
		System.out.println(sbNotFiltered4.toString());
		assertTrue(sbNotFiltered4.toString().contains("\n  Joe\n  35\n"));
		assertTrue(sbNotFiltered4.toString().contains("\n  Jill\n  23\n"));
		assertTrue(sbNotFiltered4.toString().contains("\n  Jenny\n  26\n"));
		assertTrue(sbNotFiltered4.toString().contains("\n  Johanna\n  26\n"));
		
		// test getting change callbacks and consistency callbacks
		MyChangeCallback changeCallback = new MyChangeCallback();
		liveView.addChangeListener(changeCallback);
		MyConsistencyCallback consistencyCallback = new MyConsistencyCallback();
		changeAwareMondixInstance.addConsistencyListener(consistencyCallback);
		ImmutableMap<String, Object> age7 = ImmutableMap.<String, Object>builder()
			    .put("name", "Jake").put("year", 29).build();
		db.addRow("age", age7);
		db.removeRow("age", age5);
		System.out.println(changeCallback.getNotificationsStr());
		assertTrue(changeCallback.getNotificationsStr().toString().contains("inserted: true; tuple: [Jake, 29]"));
		assertTrue(changeCallback.getNotificationsStr().toString().contains("inserted: false; tuple: [Johanna, 26]"));
		System.out.println(consistencyCallback.getNumberOfCallbacks());
		assertEquals(2, consistencyCallback.getNumberOfCallbacks()); // one after addition, one after deletion
		
		// test whether Johanna is deleted from the filtered results, as the callback says
		StringBuilder sbFiltered4 = new StringBuilder();
		for(List<?> tuple : liveViewFiltered.getAllTuples()) {
			sbFiltered4.append(tupleToString(tuple));
		}
		System.out.println(sbFiltered4.toString());
		assertFalse(sbFiltered4.toString().contains("\n  Joe\n  35\n"));
		assertFalse(sbFiltered4.toString().contains("\n  Jill\n  23\n"));
		assertTrue(sbFiltered4.toString().contains("\n  Jenny\n  26\n"));
		assertFalse(sbFiltered4.toString().contains("\n  Johanna\n  26\n"));
    }
	

	@Test
	public void addRelation() throws Exception {
		// test adding relation to change-aware instances
		
		// create age relation, bike relation does not exist
		IChangeAwareMondixInstance changeAwareMondixInstance = setupChangeAwareMI();
		Iterable<? extends Object> relationNames = changeAwareMondixInstance.getPublishedRelationNames().getValues();
		StringBuilder sb = new StringBuilder();
		for(Object relationName : relationNames) {
			sb.append((String) relationName).append("\n"); 
		}
		System.out.println(sb);
		assertTrue(sb.toString().contains("age"));
		assertFalse(sb.toString().contains("bike"));
		
		// add bike relation
		HashSet<ImmutableMap<String, Object>> bikes = createBikesRelation();
		db.addRelation("bike", bikes, null);
		relationNames = changeAwareMondixInstance.getPublishedRelationNames().getValues();
		sb = new StringBuilder();
		for(Object relationName : relationNames) {
			sb.append((String) relationName).append("\n"); 
		}
		System.out.println(sb);
		assertTrue(sb.toString().contains("age"));
		assertTrue(sb.toString().contains("bike"));
		
		// remove age relation from live mondix instance
		db.removeRelation("age");
		relationNames = changeAwareMondixInstance.getPublishedRelationNames().getValues();
		sb = new StringBuilder();
		for(Object relationName : relationNames) {
			sb.append((String) relationName).append("\n"); 
		}
		System.out.println(sb);
		assertFalse(sb.toString().contains("age"));
		assertTrue(sb.toString().contains("bike"));

	}
	
}

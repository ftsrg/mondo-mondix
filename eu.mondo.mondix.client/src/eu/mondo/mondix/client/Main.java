package eu.mondo.mondix.client;

public class Main {

	public static void main(String[] args) throws Exception {
		MondixTest test = new MondixTest();
		
		test.setUp();
		test.testPublishedRelations();
		test.testCatalogRelation();
		test.testAgeRelation();
		test.testUnary();
		test.testNullary();
		test.live();
	}
	
}

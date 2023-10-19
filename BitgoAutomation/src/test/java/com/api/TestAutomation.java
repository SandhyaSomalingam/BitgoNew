package com.api;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class TestAutomation {

	SoftAssert soft;

	@Test(priority = 1)
	public void testCase2() {

		String endpoints[] = { "/100", "/200", "/300" };

		// baseURI
		RestAssured.baseURI = "https://blockstream.info/api//block/000000000000000000076c036ff5119e5a5a74df77abf64203473364509f7732/txid";

		String txnIDS[] = new String[endpoints.length];
		for (int i = 0; i < endpoints.length; i++) {

			txnIDS[i] = RestAssured.given().get(endpoints[i]).getBody().asString();

		}

		RestAssured.baseURI = "https://blockstream.info/api/tx";

		String txnDetailsEP1 = "/" + txnIDS[0];
		String txnDetailsEP2 = "/" + txnIDS[1];
		String txnDetailsEP3 = "/" + txnIDS[2];

		Response txnDetailsresp1 = RestAssured.given().get(txnDetailsEP1);
		Response txnDetailsresp2 = RestAssured.given().get(txnDetailsEP2);
		Response txnDetailsresp3 = RestAssured.given().get(txnDetailsEP3);

		// Size of VIN
		int size1 = txnDetailsresp1.jsonPath().getList("vin").size();
		int size2 = txnDetailsresp2.jsonPath().getList("vin").size();
		int size3 = txnDetailsresp3.jsonPath().getList("vin").size();

		int sumOfInputs = size1 + size2 + size3;

		int size4 = txnDetailsresp1.jsonPath().getList("vout").size();
		int size5 = txnDetailsresp2.jsonPath().getList("vout").size();
		int size6 = txnDetailsresp3.jsonPath().getList("vout").size();

		int sumOfOutputs = size4 + size5 + size6;

		soft = new SoftAssert();

		soft.assertEquals(sumOfInputs, 5);
		soft.assertEquals(sumOfOutputs, 4);

	}

	@Test(priority = 2)
	public void testCase1() {

		RestAssured.baseURI = "https://blockstream.info/api/block-height/680000";
		Response response1 = RestAssured.given().get();
		String blockId = response1.getBody().asString();

		RestAssured.baseURI = "https://blockstream.info/api/block";
		String endpoint1 = "/" + blockId;
		Response response2 = RestAssured.given().get(endpoint1);

		String txCount1 = response2.jsonPath().getString("tx_count");

		soft.assertEquals(2875, txCount1);
		soft.assertEquals(2875, txCount1, "The Transaction Count Doest Not Match , Failed");

	}

	@AfterClass
	public void assertAllMethod() {

		soft.assertAll(); // To Throw the Failed Exception

	}

}

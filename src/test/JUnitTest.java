package test;

import org.junit.*;
import static org.junit.Assert.*;

import evaluators.*;
import resources.*;

public class JUnitTest {
	@Test
	public void intTest() {
		try {
		IntegerEval test1 = new IntegerEval();
		int value1 = 12;
		int value2 = 5;
		System.out.println("Testing Integer Evaluation for:");
		System.out.println(value1+ "+" + value2);
		assertTrue(test1.evaluate(value1 + "+" + value2) == value1 + value2);
		System.out.println(value1 + " + " + value2);
		assertTrue(test1.evaluate(value1 + " + " + value2) == value1 + value2);
		System.out.println(value1 + "    +                  " + value2);
		assertTrue(test1.evaluate(value1 + "    +                  " + value2)
															== value1 + value2);
		} catch(NoSuchVariableExistsException e) {
		} catch(InvalidExpressionException e) {
		} catch(VariableAssignmentException e) {
		} catch(DivideByZeroException e) {
		} catch(CircularReferenceException e) {
		}
	}

	@Test
	public void doubleTest() {
		try {
		DoubleEval test2 = new DoubleEval();
		double value1 = 3.14159;
		double value2 = 1.0;
		System.out.println("Testing Double Evaluation for:");
		System.out.println(value1 + "+" + value2);
		assertTrue(test2.evaluate(value1 + "+" + value2) == value1 + value2);
		System.out.println(value1 + " + " + value2);
		assertTrue(test2.evaluate(value1 + " + " + value2) == value1 + value2);
		System.out.println(value1 + "    +                  " + value2);
		assertTrue(test2.evaluate(value1 + "    +                  " + value2)
															== value1 + value2);
		} catch(NoSuchVariableExistsException e) {
		} catch(InvalidExpressionException e) {
		} catch(VariableAssignmentException e) {
		} catch(DivideByZeroException e) {
		} catch(CircularReferenceException e) {
		}
	}

	@Test
	public void floatTest() {
		try {
		FloatEval test3 = new FloatEval();
		float value1 = 3.14159f;
		float value2 = 4.2f;
		System.out.println("Testing Float Evaluation for:");
		System.out.println(value1 + "+" + value2);
		assertTrue(test3.evaluate(value1 + "+" + value2) == value1 + value2);
		System.out.println(value1 + " + " + value2);
		assertTrue(test3.evaluate(value1 + " + " + value2) == value1 + value2);
		System.out.println(value1 + "    +                  " + value2);
		assertTrue(test3.evaluate(value1 + "    +                  " + value2)
															== value1 + value2);
		} catch(NoSuchVariableExistsException e) {
		} catch(InvalidExpressionException e) {
		} catch(VariableAssignmentException e) {
		} catch(DivideByZeroException e) {
		} catch(CircularReferenceException e) {
		}
	}

	@Test
	public void stringTest() {
		try {
		StringEval test4 = new StringEval();
		String value1 = "\"Bat\"";
		String value2 = "\"man\"";
		String value3 = "\"Batman\"";
		System.out.println("Testing String Evaluation for:");
		System.out.println(value1 + "+" + value2);
		assertTrue(test4.evaluate(value1 + "+" + value2).equals(value3));
		System.out.println(value1 + " + " + value2);
		assertTrue(test4.evaluate(value1 + " + " + value2).equals(value3));
		System.out.println(value1 + "    +                  " + value2);
		assertTrue(test4.evaluate(value1 + "    +                  " + value2)
															.equals(value3));
		} catch(NoSuchVariableExistsException e) {
		} catch(InvalidExpressionException e) {
		} catch(VariableAssignmentException e) {
		} catch(DivideByZeroException e) {
		} catch(CircularReferenceException e) {
		}
	}

	@Test
	public void expTest() {
		// Testing the use of parentheses and exponents
		try {
		IntegerEval test5 = new IntegerEval();
		DoubleEval test5_1 = new DoubleEval();
		FloatEval test5_2 = new FloatEval();
		System.out.println("Testing Integer Evaluation for: (3^3)^2");
		assertTrue(test5.evaluate("(3^3)^2") == Math.pow(Math.pow(3, 3), 2));
		System.out.println("Testing Double Evaluation for: (3.0^3.0)^2.0");
		assertTrue(test5_1.evaluate("(3.0^3.0)^2.0")
										== Math.pow(Math.pow(3.0, 3.0), 2.0));
		System.out.println("Testing Float Evaluation for: (3.0f^3.0f)^2.0f");
		assertTrue(test5_2.evaluate("(3.0^3.0)^2.0")
									== Math.pow(Math.pow(3.0f, 3.0f), 2.0f));
		} catch(NoSuchVariableExistsException e) {
		} catch(InvalidExpressionException e) {
		} catch(VariableAssignmentException e) {
		} catch(DivideByZeroException e) {
		} catch(CircularReferenceException e) {
		}
	}

	@Test
	public void modTest() {
		// Testing the use of parentheses and modulus
		try {
		IntegerEval test6 = new IntegerEval();
		System.out.println("Testing Integer Evaluation for: (49%7)+2");
		assertTrue(test6.evaluate("(49%7)+2") == (49 % 7) + 2);
		} catch(NoSuchVariableExistsException e) {
		} catch(InvalidExpressionException e) {
		} catch(VariableAssignmentException e) {
		} catch(DivideByZeroException e) {
		} catch(CircularReferenceException e) {
		}
	}

	@Test
	public void negNumTest() {
		// Testing negative numbers
		try {
		IntegerEval test8 = new IntegerEval();
		DoubleEval test8_1 = new DoubleEval();
		FloatEval test8_2 = new FloatEval();
		System.out.println("Testing Integer Evaluation for: ~1-2");
		assertTrue(test8.evaluate("~1-2") == -1 - 2);
		System.out.println("Testing Double Evaluation for: ~1.0-2.0");
		assertTrue(test8_1.evaluate("~1.0-2.0") == -1.0 - 2.0);
		System.out.println("Testing Float Evaluation for: ~1.0f-2.0f");
		assertTrue(test8_2.evaluate("~1.0-2.0") == -1.0f - 2.0f);
		} catch(NoSuchVariableExistsException e) {
		} catch(InvalidExpressionException e) {
		} catch(VariableAssignmentException e) {
		} catch(DivideByZeroException e) {
		} catch(CircularReferenceException e) {
		}
	}

	@Test
	public void varTest() {
		// Testing variables
 		try {
		StringEval test9_1 = new StringEval();
		IntegerEval test9_2 = new IntegerEval();
		Tools.addVariable("x1:1");
		Tools.addVariable("x2:2");
		Tools.addVariable("x3:3");
		Tools.addVariable("str1:\"foo\"");
		Tools.addVariable("str2:\"bar\"");
		// Should set variable temp to 1234567890
		System.out.println("Testing variable assignment: temp:1234567890");
		Tools.addVariable("temp:1234567890");
		assertTrue(Tools.variableExists("temp") == true);
		System.out.println("Variable str1:\"foo\"");
		System.out.println("Variable str2:\"bar\"");
		System.out.println("Testing Variable Evaluation for: str1 + str2");
		assertTrue(test9_1.evaluate("str1+str2").equals("\"foobar\""));
		System.out.println("Testing Variable Evaluation for: x1 + x2 + x3");
		assertTrue(test9_2.evaluate("x1+x2+x3") == 6);
 		} catch(NoSuchVariableExistsException e) {
		} catch(InvalidExpressionException e) {
		} catch(VariableAssignmentException e) {
		} catch(DivideByZeroException e) {
		} catch(CircularReferenceException e) {
		}
	}

	@Test
	public void boolTest() {
		// Testing Boolean expressions using the below operators
		// true, false, <, >, =, !, <=, >=, &, |, +, -, *, /, ^, (, )
		try {
		BooleanEval test10 = new BooleanEval();
		System.out.println("Testing Boolean Evaluation for: 1 + 2 = 9 - 6");
		assertTrue(test10.evaluate("1 + 2 = 9 - 6") == true);
		System.out.println("Testing Boolean Evaluation for: 2.0 < 5.0");
		assertTrue(test10.evaluate("2.0 < 5.0") == true);
		System.out.println("Testing Boolean Evaluation for: 176 > 27");
		assertTrue(test10.evaluate("176 > 27") == true);
		System.out.println("Testing Boolean Evaluation for: !false");
		assertTrue(test10.evaluate("!false") == true);
		System.out.println("Testing Boolean Evaluation for: true & false");
		assertTrue(test10.evaluate("true & false") == false);
		System.out.println("Testing Boolean Evaluation for: (16*2)<=(17*2)");
		assertTrue(test10.evaluate("(16*2)<=(17*2)") == true);
		System.out.println("Testing Boolean Evaluation for: 30/5 >= 12/6");
		assertTrue(test10.evaluate("30/5 >= 12/6") == true);
		System.out.println("Testing Boolean Evaluation for: (1=2) || (3>10)");
		assertTrue(test10.evaluate("(1=2) || (3>10)") == true);
		System.out.println("Testing Boolean Evaluation for: (3^2) < (5^2)");
		assertTrue(test10.evaluate("(3^2) < (5^2)") == true);
		} catch(NoSuchVariableExistsException e) {
		} catch(InvalidExpressionException e) {
		} catch(VariableAssignmentException e) {
		} catch(DivideByZeroException e) {
		} catch(CircularReferenceException e) {
		}
	}
}// end JUnitTesting class
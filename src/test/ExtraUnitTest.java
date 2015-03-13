package test;

import org.junit.*;
import static org.junit.Assert.*;

import evaluators.*;
import resources.*;

public class ExtraUnitTest {
	
	double delta = .0000001;
	
	@Test
	public void test_1a()
	{
		try
		{
			Tools.addVariable("a1:7");
			IntegerEval test = new IntegerEval();
			int result = test.evaluate("a1");
			assertEquals(7, result);
		}
		catch(NoSuchVariableExistsException e) { }
		catch(InvalidExpressionException e) { }
		catch(VariableAssignmentException e) { } 
		catch(DivideByZeroException e) { }
		catch(CircularReferenceException e) { }
	}
	
	@Test
	public void test_1b()
	{
		try
		{
			Tools.addVariable("a1:7");
			IntegerEval test = new IntegerEval();
			int result = test.evaluate("(a1)");
			assertEquals(7, result);
		}
		catch(NoSuchVariableExistsException e) { }
		catch(InvalidExpressionException e) { }
		catch(VariableAssignmentException e) { }
		catch(DivideByZeroException e) { }
		catch(CircularReferenceException e) { }
	}
	
	@Test
	public void test_1c()
	{
		try
		{
			Tools.addVariable("a1:7");
			IntegerEval test = new IntegerEval();
			int result = test.evaluate("((((a1))))");
			assertEquals(7, result);
		}
		catch(NoSuchVariableExistsException e) { }
		catch(InvalidExpressionException e) { }
		catch(VariableAssignmentException e) { }
		catch(DivideByZeroException e) { }
		catch(CircularReferenceException e) { }
	}
	
	@Test(expected=InvalidExpressionException.class)
	public void test_1d() throws InvalidExpressionException
	{
		try
		{
			Tools.addVariable("a1:7");
			IntegerEval test = new IntegerEval();
			int result = test.evaluate("((((a1)))))");
			assertEquals(7, result);
		}
		catch(NoSuchVariableExistsException e) { }
		catch(VariableAssignmentException e) { }
		catch(DivideByZeroException e) { }
		catch(CircularReferenceException e) { }
	}
	
	@Test(expected=InvalidExpressionException.class)
	public void test_1e() throws InvalidExpressionException
	{
		try
		{
			Tools.addVariable("a1:7");
			IntegerEval test = new IntegerEval();
			int result = test.evaluate("(((((a1))))");
			assertEquals(7, result);
		}
		catch(NoSuchVariableExistsException e) { }
		catch(VariableAssignmentException e) { }
		catch(DivideByZeroException e) { }
		catch(CircularReferenceException e) { }
	}
	
	@Test
	public void test_int_2a()
	{
		try
		{
			IntegerEval test = new IntegerEval();
			int result = test.evaluate("2*3*4");
			assertEquals(24, result);
		}
		catch(NoSuchVariableExistsException e) { }
		catch(InvalidExpressionException e) { }
		catch(VariableAssignmentException e) { }
		catch(DivideByZeroException e) { }
		catch(CircularReferenceException e) { }
	}
	
	@Test
	public void test_int_2b()
	{
		try
		{
			IntegerEval test = new IntegerEval();
			int result = test.evaluate("16/4");
			assertEquals(4, result);
		}
		catch(NoSuchVariableExistsException e) { }
		catch(InvalidExpressionException e) { }
		catch(VariableAssignmentException e) { }
		catch(DivideByZeroException e) { }
		catch(CircularReferenceException e) { }
	}
	
	@Test
	public void test_int_2c()
	{
		try
		{
			IntegerEval test = new IntegerEval();
			int result = test.evaluate("17/4");
			assertEquals(4, result);
		}
		catch(NoSuchVariableExistsException e) { }
		catch(InvalidExpressionException e) { }
		catch(VariableAssignmentException e) { }
		catch(DivideByZeroException e) { }
		catch(CircularReferenceException e) { }
	}
	
	@Test
	public void test_int_2d()
	{
		try
		{
			IntegerEval test = new IntegerEval();
			int result = test.evaluate("71+2+3");
			assertEquals(76, result);
		}
		catch(NoSuchVariableExistsException e) { }
		catch(InvalidExpressionException e) { }
		catch(VariableAssignmentException e) { }
		catch(DivideByZeroException e) { }
		catch(CircularReferenceException e) { }
	}
	
	@Test
	public void test_int_2e()
	{
		try
		{
			IntegerEval test = new IntegerEval();
			int result = test.evaluate("99-42-6");
			assertEquals(51, result);
		}
		catch(NoSuchVariableExistsException e) { }
		catch(InvalidExpressionException e) { }
		catch(VariableAssignmentException e) { }
		catch(DivideByZeroException e) { }
		catch(CircularReferenceException e) { }
	}
	
	@Test
	public void test_int_2f()
	{
		try
		{
			IntegerEval test = new IntegerEval();
			int result = test.evaluate("(3^3)^2");
			assertEquals(729, result);
		}
		catch(NoSuchVariableExistsException e) { }
		catch(InvalidExpressionException e) { }
		catch(VariableAssignmentException e) { }
		catch(DivideByZeroException e) { }
		catch(CircularReferenceException e) { }
	}
	
	@Test
	public void test_int_2g()
	{
		try
		{
			IntegerEval test = new IntegerEval();
			int result = test.evaluate("3^3^2");
			assertEquals(729, result);
		}
		catch(NoSuchVariableExistsException e) { }
		catch(InvalidExpressionException e) { }
		catch(VariableAssignmentException e) { }
		catch(DivideByZeroException e) { }
		catch(CircularReferenceException e) { }
	}
	
	@Test
	public void test_int_2h()
	{
		try
		{
			IntegerEval test = new IntegerEval();
			int result = test.evaluate("7+2*3");
			assertEquals(13, result);
		}
		catch(NoSuchVariableExistsException e) { }
		catch(InvalidExpressionException e) { }
		catch(VariableAssignmentException e) { }
		catch(DivideByZeroException e) { }
		catch(CircularReferenceException e) { }
	}
	
	@Test
	public void test_int_2i()
	{
		try
		{
			IntegerEval test = new IntegerEval();
			int result = test.evaluate("7*(2+3)*((((2+1))))");
			assertEquals(105, result);
		}
		catch(NoSuchVariableExistsException e) { }
		catch(InvalidExpressionException e) { }
		catch(VariableAssignmentException e) { }
		catch(DivideByZeroException e) { }
		catch(CircularReferenceException e) { }
	}
	
	@Test
	public void test_int_2j()
	{
		try
		{
			IntegerEval test = new IntegerEval();
			int result = test.evaluate("((7*(2+3)*((((2+1))))-15)/10)^2");
			assertEquals(81, result);
		}
		catch(NoSuchVariableExistsException e) { }
		catch(InvalidExpressionException e) { }
		catch(VariableAssignmentException e) { }
		catch(DivideByZeroException e) { }
		catch(CircularReferenceException e) { }
	}
	
	@Test(expected=InvalidExpressionException.class)
	public void test_int_2k() throws InvalidExpressionException
	{
		try
		{
			IntegerEval test = new IntegerEval();
			int result = test.evaluate("7*");
			assertEquals(81, result);
		}
		catch(NoSuchVariableExistsException e) { }
		catch(VariableAssignmentException e) { }
		catch(DivideByZeroException e) { }
		catch(CircularReferenceException e) { }
	}
	
	@Test(expected=DivideByZeroException.class)
	public void test_int_2l() throws DivideByZeroException
	{
		try
		{
			IntegerEval test = new IntegerEval();
			int result = test.evaluate("7/0");
			assertEquals(Double.NaN, result, delta);
		}
		catch(NoSuchVariableExistsException e) { }
		catch(InvalidExpressionException e) { }
		catch(VariableAssignmentException e) { }
		catch(CircularReferenceException e) { }
	}
	
	@Test
	public void test_double_2a()
	{
		try
		{
			DoubleEval test = new DoubleEval();
			double result = test.evaluate("2*3*4");
			assertEquals(24, result, delta);
		}
		catch(NoSuchVariableExistsException e) { }
		catch(InvalidExpressionException e) { }
		catch(VariableAssignmentException e) { }
		catch(DivideByZeroException e) { }
		catch(CircularReferenceException e) { }
	}
	
	@Test
	public void test_double_2b()
	{
		try
		{
			DoubleEval test = new DoubleEval();
			double result = test.evaluate("16/4");
			assertEquals(4, result, delta);
		}
		catch(NoSuchVariableExistsException e) { }
		catch(InvalidExpressionException e) { }
		catch(VariableAssignmentException e) { }
		catch(DivideByZeroException e) { }
		catch(CircularReferenceException e) { }
	}
	
	@Test
	public void test_double_2c()
	{
		try
		{
			DoubleEval test = new DoubleEval();
			double result = test.evaluate("17/4");
			assertEquals(4.25, result, delta);
		}
		catch(NoSuchVariableExistsException e) { }
		catch(InvalidExpressionException e) { }
		catch(VariableAssignmentException e) { }
		catch(DivideByZeroException e) { }
		catch(CircularReferenceException e) { }
	}
	
	@Test
	public void test_double_2d()
	{
		try
		{
			DoubleEval test = new DoubleEval();
			double result = test.evaluate("71+2+3");
			assertEquals(76, result, delta);
		}
		catch(NoSuchVariableExistsException e) { }
		catch(InvalidExpressionException e) { }
		catch(VariableAssignmentException e) { }
		catch(DivideByZeroException e) { }
		catch(CircularReferenceException e) { }
	}
	
	@Test
	public void test_double_2e()
	{
		try
		{
			DoubleEval test = new DoubleEval();
			double result = test.evaluate("99-42-6");
			assertEquals(51, result, delta);
		}
		catch(NoSuchVariableExistsException e) { }
		catch(InvalidExpressionException e) { }
		catch(VariableAssignmentException e) { }
		catch(DivideByZeroException e) { }
		catch(CircularReferenceException e) { }
	}
	
	@Test
	public void test_double_2f()
	{
		try
		{
			DoubleEval test = new DoubleEval();
			double result = test.evaluate("(3^3)^2");
			assertEquals(729, result, delta);
		}
		catch(NoSuchVariableExistsException e) { }
		catch(InvalidExpressionException e) { }
		catch(VariableAssignmentException e) { }
		catch(DivideByZeroException e) { }
		catch(CircularReferenceException e) { }
	}
	
	@Test
	public void test_double_2g()
	{
		try
		{
			DoubleEval test = new DoubleEval();
			double result = test.evaluate("3^3^2");
			assertEquals(729, result, delta);
		}
		catch(NoSuchVariableExistsException e) { }
		catch(InvalidExpressionException e) { }
		catch(VariableAssignmentException e) { }
		catch(DivideByZeroException e) { }
		catch(CircularReferenceException e) { }
	}
	
	@Test
	public void test_double_2h()
	{
		try
		{
			DoubleEval test = new DoubleEval();
			double result = test.evaluate("7+2*3");
			assertEquals(13, result, delta);
		}
		catch(NoSuchVariableExistsException e) { }
		catch(InvalidExpressionException e) { }
		catch(VariableAssignmentException e) { }
		catch(DivideByZeroException e) { }
		catch(CircularReferenceException e) { }
	}
	
	@Test
	public void test_double_2i()
	{
		try
		{
			DoubleEval test = new DoubleEval();
			double result = test.evaluate("7*(2+3)*((((2+1))))");
			assertEquals(105, result, delta);
		}
		catch(NoSuchVariableExistsException e) { }
		catch(InvalidExpressionException e) { }
		catch(VariableAssignmentException e) { }
		catch(DivideByZeroException e) { }
		catch(CircularReferenceException e) { }
	}
	
	@Test
	public void test_double_2j()
	{
		try
		{
			DoubleEval test = new DoubleEval();
			double result = test.evaluate("((7*(2+3)*((((2+1))))-15)/10)^2");
			assertEquals(81, result, delta);
		}
		catch(NoSuchVariableExistsException e) { }
		catch(InvalidExpressionException e) { }
		catch(VariableAssignmentException e) { }
		catch(DivideByZeroException e) { }
		catch(CircularReferenceException e) { }
	}
	
	@Test(expected=InvalidExpressionException.class)
	public void test_double_2k() throws InvalidExpressionException
	{
		try
		{
			DoubleEval test = new DoubleEval();
			double result = test.evaluate("7*");
			assertEquals(81, result, delta);
		}
		catch(NoSuchVariableExistsException e) { }
		catch(VariableAssignmentException e) { }
		catch(DivideByZeroException e) { }
		catch(CircularReferenceException e) { }
	}
	
	@Test(expected=DivideByZeroException.class)
	public void test_double_2l() throws DivideByZeroException
	{
		try
		{
			DoubleEval test = new DoubleEval();
			double result = test.evaluate("7/0");
			assertEquals(Double.NaN, result, delta);
		}
		catch(NoSuchVariableExistsException e) { }
		catch(InvalidExpressionException e) { }
		catch(VariableAssignmentException e) { }
		catch(CircularReferenceException e) { }
	}
	
	@Test
	public void test_3a()
	{
		try
		{
			Tools.addVariable("a1:8");
			Tools.addVariable("a2:3");
			IntegerEval test = new IntegerEval();
			int result = test.evaluate("a1*(a1-a2)+a2/3");
			assertEquals(41,result);
		}
		catch(NoSuchVariableExistsException e) { }
		catch(InvalidExpressionException e) { }
		catch(VariableAssignmentException e) { }
		catch(DivideByZeroException e) { }
		catch(CircularReferenceException e) { }
	}
	
	@Test
	public void test_4a()
	{
		try
		{
			Tools.addVariable("a1:10");
			Tools.addVariable("b1:7");
			IntegerEval test = new IntegerEval();
			int result = test.evaluate("a1+b1");
			Tools.addVariable("a2:" + result);
			assertEquals(17,result);
		}
		catch(NoSuchVariableExistsException e) { }
		catch(InvalidExpressionException e) { }
		catch(VariableAssignmentException e) { }
		catch(DivideByZeroException e) { }
		catch(CircularReferenceException e) { }
	}
	
	
	@Test
	public void test_4b()
	{
		try
		{
			IntegerEval test = new IntegerEval();
			int result = test.evaluate("a2");
			Tools.addVariable("b2:" + result);
			assertEquals(17,result);
		}
		catch(NoSuchVariableExistsException e) { }
		catch(InvalidExpressionException e) { }
		catch(VariableAssignmentException e) { }
		catch(DivideByZeroException e) { }
		catch(CircularReferenceException e) { }
	}
	
	@Test
	public void test_4c()
	{
		try
		{
			IntegerEval test = new IntegerEval();
			int result = test.evaluate("a2+b2");
			Tools.addVariable("a3:" + result);
			assertEquals(34,result);
		}
		catch(NoSuchVariableExistsException e) { }
		catch(InvalidExpressionException e) { }
		catch(VariableAssignmentException e) { }
		catch(DivideByZeroException e) { }
		catch(CircularReferenceException e) { }
	}
	
	@Test
	public void test_4d()
	{
		try
		{
			IntegerEval test = new IntegerEval();
			int result = test.evaluate("a3-a2");
			Tools.addVariable("b3:" + result);
			assertEquals(17,result);
		}
		catch(NoSuchVariableExistsException e) { }
		catch(InvalidExpressionException e) { }
		catch(VariableAssignmentException e) { }
		catch(DivideByZeroException e) { }
		catch(CircularReferenceException e) { }
	}
	
	@Test
	public void test_4e()
	{
		try
		{
			IntegerEval test = new IntegerEval();
			int result = test.evaluate("a3");
			Tools.addVariable("a4:" + result);
			assertEquals(34,result);
		}
		catch(NoSuchVariableExistsException e) { }
		catch(InvalidExpressionException e) { }
		catch(VariableAssignmentException e) { }
		catch(DivideByZeroException e) { }
		catch(CircularReferenceException e) { }
	}
	
	@Test
	public void test_4f()
	{
		try
		{
			IntegerEval test = new IntegerEval();
			int result = test.evaluate("a4+b3");
			Tools.addVariable("b4:" + result);
			assertEquals(51,result);
		}
		catch(NoSuchVariableExistsException e) { }
		catch(InvalidExpressionException e) { }
		catch(VariableAssignmentException e) { }
		catch(DivideByZeroException e) { }
		catch(CircularReferenceException e) { }
	}
}
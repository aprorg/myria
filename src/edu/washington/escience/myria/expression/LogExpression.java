package edu.washington.escience.myria.expression;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import edu.washington.escience.myria.Schema;
import edu.washington.escience.myria.Type;

/**
 * Take the natural logarithm (base e) of the operand.
 */
public class LogExpression extends UnaryExpression {
  /***/
  private static final long serialVersionUID = 1L;

  /**
   * This is not really unused, it's used automagically by Jackson deserialization.
   */
  @SuppressWarnings("unused")
  private LogExpression() {
    super();
  }

  /**
   * Take the natural logarithm (base e) of the operand.
   * 
   * @param operand the operand.
   */
  public LogExpression(final ExpressionOperator operand) {
    super(operand);
  }

  @Override
  public Type getOutputType(final Schema schema) {
    Type operandType = getOperand().getOutputType(schema);
    ImmutableList<Type> validTypes = ImmutableList.of(Type.DOUBLE_TYPE, Type.FLOAT_TYPE, Type.LONG_TYPE, Type.INT_TYPE);
    int operandIdx = validTypes.indexOf(operandType);
    Preconditions.checkArgument(operandIdx != -1, "%s cannot handle operand [%s] of Type %s", getClass()
        .getSimpleName(), getOperand(), operandType);
    return Type.DOUBLE_TYPE;
  }

  @Override
  public String getJavaString(final Schema schema) {
    return getFunctionCallUnaryString("Math.log", schema);
  }

  @Override
  public int hashCode() {
    return defaultHashCode();
  }

  @Override
  public boolean equals(final Object other) {
    if (other == null || !(other instanceof LogExpression)) {
      return false;
    }
    LogExpression otherExpr = (LogExpression) other;
    return Objects.equal(getOperand(), otherExpr.getOperand());
  }
}
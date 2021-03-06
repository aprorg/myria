package edu.washington.escience.myria.operator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.washington.escience.myria.DbException;
import edu.washington.escience.myria.Type;
import edu.washington.escience.myria.storage.TupleBatch;
import edu.washington.escience.myria.util.JoinTestUtils;
import edu.washington.escience.myria.util.TestEnvVars;

public class SymmetricHashCountingJoinTest {

  @Test
  public void testSymmetricHashCountingJoin() throws DbException {
    TupleSource left = new TupleSource(JoinTestUtils.leftInput);
    TupleSource right = new TupleSource(JoinTestUtils.rightInput);
    Operator join = new SymmetricHashCountingJoin(left, right, new int[] { 0, 1, 2 }, new int[] { 1, 2, 0 });
    join.open(TestEnvVars.get());
    assertEquals(1, join.getSchema().numColumns());
    assertEquals(Type.LONG_TYPE, join.getSchema().getColumnType(0));
    long count = 0;
    while (!join.eos()) {
      TupleBatch tb = join.nextReady();
      if (tb == null) {
        continue;
      }
      for (int row = 0; row < tb.numTuples(); ++row) {
        count += tb.getLong(0, row);
      }
    }
    join.close();
    assertEquals(7L, count);
  }

  @Test(expected = IllegalStateException.class)
  public void testIncompatibleJoinKeys() throws DbException {
    TupleSource left = new TupleSource(JoinTestUtils.leftInput);
    TupleSource right = new TupleSource(JoinTestUtils.rightInput);
    Operator join = new SymmetricHashCountingJoin(left, right, new int[] { 0 }, new int[] { 0 });
    join.open(TestEnvVars.get());
  }
}

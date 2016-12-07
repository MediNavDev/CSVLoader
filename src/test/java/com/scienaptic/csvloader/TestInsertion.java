package com.scienaptic.csvloader;

import com.google.common.base.Stopwatch;
import com.scienaptic.csvloader.db.DatasourceType;
import com.scienaptic.csvloader.db.JdbcDetails;
import com.scienaptic.csvloader.db.SchemaBuilder2;
import com.scienaptic.csvloader.utils.Tuple2;
import com.scienaptic.csvloader.utils.TypeUtil;
import org.junit.Test;
import scala.collection.JavaConversions;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class TestInsertion {
  private CsvParserConfig config = CsvParserConfig.defaultBuilder
      .lineSeparator("\r\n")
      .build();
  private final JdbcDetails details = new JdbcDetails("jdbc:mysql://localhost:3306/csvDB?createIfNotExists=true", "root", "root");
  private final String[] files = {"/Users/apple/Downloads/scienaptic.datasets/HACKER_EARTH_dataset/users.csv",
      "/Users/apple/Downloads/scienaptic.datasets/HACKER_EARTH_dataset/submissions.csv",
      "/Users/apple/Downloads/scienaptic.datasets/HACKER_EARTH_dataset/problems.csv",
      "/Users/apple/Downloads/german_data.csv",
      "/Users/apple/Downloads/scienaptic.datasets/bfl.data/BFL_Data_Part1.csv"
  };
  private final int[] rowCounts = {62530, 1_198_131, 1002, 999, 9_999_999};

  @Test
  public void test() throws IOException, SQLException {
    for (int i = 0; i < files.length; i++) {
      if (i == 4)
        config = CsvParserConfig.newBuilder(config).fieldSeparator('|').build();

      Tuple2<String, String> pair = queries(config, Paths.get(files[i]));
      Stopwatch stopwatch = Stopwatch.createStarted();
      createTable(pair.left, pair.right, rowCounts[i]);
      stopwatch.stop();
      System.out.println("Time taken to insert: " + stopwatch.toString());
    }
  }

  private Tuple2<String, String> queries(CsvParserConfig config, Path path)
      throws IOException, SQLException {
    List<Column> sample = Reader.sample(path, config, .1f);
    assertNotNull(sample);
    assertTrue(!sample.isEmpty());

    List<Field> fields = TypeInferer.inferFieldTypes(sample);
    assertNotNull(fields);
    assertTrue(!fields.isEmpty());

    String table = path.toFile().getName().replace('.', '_');
    scala.collection.immutable.List<Field> scalaList = JavaConversions.asScalaBuffer(fields).toList();
    Schema schema = new Schema("csvDB", table, scalaList);
    System.out.println("Schema: " + schema);

    String ddl = SchemaBuilder2.createDDL(schema, DatasourceType.MySQL);
    assertTrue(TypeUtil.isNotEmpty(ddl));
    String insertQry = SchemaBuilder2.createInsertQry(schema, path, config, DatasourceType.MySQL);
    assertTrue(TypeUtil.isNotEmpty(insertQry));
    return Tuple2.of(ddl, insertQry);
  }

  private void createTable(String ddl, String insertQry, int expected) throws SQLException {
    try (Connection conn = DriverManager.getConnection(details.url(), details.props())) {
      PreparedStatement pStmt = conn.prepareStatement(ddl);
      pStmt.execute();

      System.out.println("Insert query: " + insertQry);
      pStmt = conn.prepareStatement(insertQry);
      int rowsInserted = pStmt.executeUpdate();
      assertEquals("Num of rows inserted", expected, rowsInserted);
    }
  }

}

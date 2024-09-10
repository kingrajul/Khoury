package cs3500.hw1.filesystem;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ExamplarFileSystem {

  // Test that creating a SimpleDirectory with null name throws an exception
  @Test(expected = IllegalArgumentException.class)
  public void testSimpleDirectoryWithNullName() {
    new SimpleDirectory(null, List.of(), List.of());
  }

  // Test that the capacity is correctly reported for an empty file system
  @Test
  public void testCapacityOfEmptyFileSystem() {
    SimpleDirectory root = new SimpleDirectory("root", List.of(), List.of());
    ReadOnlyFileSystem fileSystem = new ReadOnlyFileSystem(10 * Size.KILOBYTE.inBytes, root);
    Assert.assertEquals("Checking capacity of filesystem", 10 * Size.KILOBYTE.inBytes, fileSystem.capacity());
  }

  // Test that prettyPrint produces the correct output for a simple directory structure
  @Test
  public void testPrettyPrintSimpleDirectory() {
    StringFile file1 = new StringFile("exp-name.log", "log content");
    SimpleDirectory experimentDir = new SimpleDirectory("experiment", List.of(), List.of(file1));
    SimpleDirectory dataDir = new SimpleDirectory("data", List.of(experimentDir), List.of());
    SimpleDirectory root = new SimpleDirectory("root", List.of(dataDir), List.of());
    ReadOnlyFileSystem fileSystem = new ReadOnlyFileSystem(1024, root);

    String expectedOutput = "+-root/\n| +-data/\n| | +-experiment/\n| | | +-exp-name.log\n";
    Assert.assertEquals("Checking pretty print output", expectedOutput, fileSystem.prettyPrint());
  }

  // Test that search returns the correct results for a given phrase in a StringFile
  @Test
  public void testSearchInStringFile() {
    StringFile file1 = new StringFile("cool-thing.txt", "This is a cool thing.");
    SimpleDirectory root = new SimpleDirectory("root", List.of(), List.of(file1));
    ReadOnlyFileSystem fileSystem = new ReadOnlyFileSystem(1024, root);

    List<StringFile> searchResults = fileSystem.search("cool");
    Assert.assertEquals("Checking search results size", 1, searchResults.size());
    Assert.assertEquals("Checking search result content", "cool-thing.txt", searchResults.get(0).getName());
  }

  // Test that totalSize in a SimpleDirectory returns the correct sum of file sizes
  @Test
  public void testTotalSizeInSimpleDirectory() {
    StringFile file1 = new StringFile("file1.txt", "content1");
    StringFile file2 = new StringFile("file2.txt", "content2");
    SimpleDirectory dir = new SimpleDirectory("dir", List.of(), List.of(file1, file2));
    Assert.assertEquals("Checking total size of directory", file1.size() + file2.size(), dir.totalSize());
  }

}


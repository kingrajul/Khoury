import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ExamplarFileSystemFull {

    // Chaffs #00-05: Testing constructors for various classes
    
    // Test that creating a SimpleDirectory with a null name throws an exception
    @Test(expected = IllegalArgumentException.class)
    public void testSimpleDirectoryWithNullName() {
        new SimpleDirectory(null, List.of(), List.of());
    }

    // Test that creating a StringFile with a null name throws an exception
    @Test(expected = IllegalArgumentException.class)
    public void testStringFileWithNullName() {
        new StringFile(null, "content");
    }

    // Test that creating a StringFile with null content throws an exception
    @Test(expected = IllegalArgumentException.class)
    public void testStringFileWithNullContent() {
        new StringFile("validName.txt", null);
    }

    // Test that creating a ReadOnlyFileSystem with a negative capacity throws an exception
    @Test(expected = IllegalArgumentException.class)
    public void testReadOnlyFileSystemWithNegativeCapacity() {
        SimpleDirectory root = new SimpleDirectory("root", List.of(), List.of());
        new ReadOnlyFileSystem(-1, root);
    }

    // Test that creating a ReadOnlyFileSystem with a null root directory throws an exception
    @Test(expected = IllegalArgumentException.class)
    public void testReadOnlyFileSystemWithNullRoot() {
        new ReadOnlyFileSystem(1024, null);
    }

    // Test that creating a SimpleDirectory with null subdirectories throws an exception
    @Test(expected = IllegalArgumentException.class)
    public void testSimpleDirectoryWithNullSubdirectories() {
        new SimpleDirectory("root", null, List.of());
    }

    // Chaffs #06-08: Testing the contents method with different signatures

    // Test contents method in SimpleDirectory with empty subdirectories and files
    @Test
    public void testContentsInEmptyDirectory() {
        SimpleDirectory emptyDir = new SimpleDirectory("empty", List.of(), List.of());
        Assert.assertTrue("Checking if contents list is empty", emptyDir.contents().isEmpty());
    }

    // Test contents method in SimpleDirectory with non-empty subdirectories and files
    @Test
    public void testContentsInNonEmptyDirectory() {
        StringFile file1 = new StringFile("file1.txt", "content1");
        SimpleDirectory subDir = new SimpleDirectory("sub", List.of(), List.of(file1));
        SimpleDirectory root = new SimpleDirectory("root", List.of(subDir), List.of(file1));

        Assert.assertEquals("Checking number of contents in root directory", 2, root.contents().size());
        Assert.assertTrue("Checking if root directory contains subdirectory", root.contents().contains(subDir));
        Assert.assertTrue("Checking if root directory contains file1", root.contents().contains(file1));
    }

    // Test contents method to differentiate between file and directory in SimpleDirectory
    @Test
    public void testContentsFileVsDirectory() {
        StringFile file = new StringFile("file.txt", "content");
        SimpleDirectory dir = new SimpleDirectory("dir", List.of(), List.of(file));

        Assert.assertEquals("Checking file type content", file, dir.contents().get(0));
        Assert.assertTrue("Checking content is instance of StringFile", dir.contents().get(0) instanceof StringFile);
    }

    // Chaffs #09-11: Testing the prettyPrint method

    // Test prettyPrint method for a single file
    @Test
    public void testPrettyPrintSingleFile() {
        StringFile file = new StringFile("file.txt", "content");
        SimpleDirectory root = new SimpleDirectory("root", List.of(), List.of(file));
        ReadOnlyFileSystem fileSystem = new ReadOnlyFileSystem(1024, root);

        String expectedOutput = "+-root/\n| +-file.txt\n";
        Assert.assertEquals("Checking pretty print output for single file", expectedOutput, fileSystem.prettyPrint());
    }

    // Test prettyPrint method for nested directories
    @Test
    public void testPrettyPrintNestedDirectories() {
        StringFile file1 = new StringFile("file1.txt", "content1");
        SimpleDirectory subDir = new SimpleDirectory("subDir", List.of(), List.of(file1));
        SimpleDirectory root = new SimpleDirectory("root", List.of(subDir), List.of());
        ReadOnlyFileSystem fileSystem = new ReadOnlyFileSystem(1024, root);

        String expectedOutput = "+-root/\n| +-subDir/\n| | +-file1.txt\n";
        Assert.assertEquals("Checking pretty print output for nested directories", expectedOutput, fileSystem.prettyPrint());
    }

    // Test prettyPrint method for an empty directory
    @Test
    public void testPrettyPrintEmptyDirectory() {
        SimpleDirectory emptyDir = new SimpleDirectory("empty", List.of(), List.of());
        SimpleDirectory root = new SimpleDirectory("root", List.of(emptyDir), List.of());
        ReadOnlyFileSystem fileSystem = new ReadOnlyFileSystem(1024, root);

        String expectedOutput = "+-root/\n| +-empty/\n";
        Assert.assertEquals("Checking pretty print output for empty directory", expectedOutput, fileSystem.prettyPrint());
    }

    // Chaffs #12-15: Testing the search method

    // Test search method for finding a phrase in StringFile
    @Test
    public void testSearchPhraseInFile() {
        StringFile file = new StringFile("log.txt", "This is a log file.");
        SimpleDirectory root = new SimpleDirectory("root", List.of(), List.of(file));
        ReadOnlyFileSystem fileSystem = new ReadOnlyFileSystem(1024, root);

        List<StringFile> results = fileSystem.search("log");
        Assert.assertEquals("Checking search result size", 1, results.size());
        Assert.assertEquals("Checking search result file name", "log.txt", results.get(0).getName());
    }

    // Test search method for no matching phrase in any files
    @Test
    public void testSearchNoMatch() {
        StringFile file = new StringFile("log.txt", "This is a log file.");
        SimpleDirectory root = new SimpleDirectory("root", List.of(), List.of(file));
        ReadOnlyFileSystem fileSystem = new ReadOnlyFileSystem(1024, root);

        List<StringFile> results = fileSystem.search("notfound");
        Assert.assertTrue("Checking no search results", results.isEmpty());
    }

    // Chaffs #16-19: Testing the totalSize method in SimpleDirectory

    // Test totalSize for an empty directory
    @Test
    public void testTotalSizeEmptyDirectory() {
        SimpleDirectory emptyDir = new SimpleDirectory("empty", List.of(), List.of());
        Assert.assertEquals("Checking total size of empty directory", 0, emptyDir.totalSize());
    }

    // Test totalSize for a directory with multiple files
    @Test
    public void testTotalSizeWithFiles() {
        StringFile file1 = new StringFile("file1.txt", "content1");
        StringFile file2 = new StringFile("file2.txt", "content2");
        SimpleDirectory dir = new SimpleDirectory("dir", List.of(), List.of(file1, file2));
        int expectedSize = file1.size() + file2.size();
        Assert.assertEquals("Checking total size of directory with files", expectedSize, dir.totalSize());
    }

    // Test totalSize for a nested directory structure
    @Test
    public void testTotalSizeNestedDirectories() {
        StringFile file1 = new StringFile("file1.txt", "content1");
        SimpleDirectory subDir = new SimpleDirectory("subDir", List.of(), List.of(file1));
        SimpleDirectory root = new SimpleDirectory("root", List.of(subDir), List.of());
        int expectedSize = file1.size();
        Assert.assertEquals("Checking total size of nested directories", expectedSize, root.totalSize());
    }
}

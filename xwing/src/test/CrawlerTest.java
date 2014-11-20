package test;

import static org.junit.Assert.*;

import org.junit.Test;

import jar.JarHelper;

import java.awt.List;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.DepthWalk.Commit;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;

import crawl.Crawler;

public class CrawlerTest {

	public static void main(String[] args) throws IOException{

	//Replace file path with desired repository
	File gitDir = new File("/Users/Adam/git/java-callgraph/.git");
	
	FileRepositoryBuilder builder = new FileRepositoryBuilder();
	Repository repo = builder.setGitDir(gitDir)
			  .readEnvironment() // scan environment GIT_* variables
			  .findGitDir() // scan up the file system tree
			  .build();
    // ... use the repository ...
	Crawler crawl = new Crawler();
	
	crawl.walkRepo(repo, "gousiosg", "java-callgraph");
	repo.close();
	}
	

}

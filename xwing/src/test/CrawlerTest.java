package test;

import static org.junit.Assert.*;

import org.junit.Test;

import jar.JarHelper;

import java.awt.List;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.DepthWalk.Commit;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import crawl.Crawler;

public class CrawlerTest {

	public static void main(String[] args) throws IOException{

	//
	File gitDir = new File("/Users/jimmyshi/git/ubc-cs410-xwing/xwing");
	
	FileRepositoryBuilder builder = new FileRepositoryBuilder();
	Repository repo = builder.setGitDir(gitDir)
			  .readEnvironment() // scan environment GIT_* variables
			  .findGitDir() // scan up the file system tree
			  .build();
    // ... use the repository ...
	//Crawler crawl = new Crawler(repo);
	Crawler crawl = new Crawler();
	crawl.walkRepo(repo);
	repo.close();
	}
}

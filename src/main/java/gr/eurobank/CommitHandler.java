package gr.eurobank;

import gr.eurobank.util.OptionsProcessor;
import gr.eurobank.versioncontrol.VersionControlHandler;
import org.apache.commons.cli.ParseException;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Map;

public class CommitHandler {

    static Logger logger = LoggerFactory.getLogger(CommitHandler.class);
    public static void main(String[] args) throws ParseException {

        OptionsProcessor optionsProcessor = new OptionsProcessor(args);
        OptionsProcessor.CmdData cmdData = optionsProcessor.processOptions();
        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
        logger.info("The repository directory is: " + cmdData.getDirectory());
        repositoryBuilder.setGitDir(new File(cmdData.getDirectory()))
                .readEnvironment()
                .findGitDir()
                .setMustExist(true);

        Map<Integer, Object[]> data = new VersionControlHandler().getCommitData(repositoryBuilder);


        //todo: send the data with rest calls
        //apache http rest client

    }
}

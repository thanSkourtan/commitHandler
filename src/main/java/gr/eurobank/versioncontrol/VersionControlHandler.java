package gr.eurobank.versioncontrol;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static gr.eurobank.util.Util.*;

public class VersionControlHandler {

    public Map<Integer, Object[]> getCommitData(FileRepositoryBuilder repositoryBuilder){

        Map<Integer, Object[]> data = new HashMap<>();

        try(Repository repository = repositoryBuilder.build()){

            //Find the differences
            Git git = new Git(repository);
            //Get the id of the tree associated to the two commits
            ObjectId head = repository.resolve("HEAD^{tree}");
            ObjectId previousHead = repository.resolve("HEAD~^{tree}");
            //Instantiate a reader to read the data from the Git database
            ObjectReader reader = repository.newObjectReader();
            //Create the tree iterator for each commit
            CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
            oldTreeIter.reset(reader, previousHead);
            CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
            newTreeIter.reset(reader, head);

            OutputStream out = new ByteArrayOutputStream();

            DiffFormatter diffFormatter = new DiffFormatter(out);
            diffFormatter.setReader(reader, repository.getConfig());
            diffFormatter.setDiffComparator(RawTextComparator.WS_IGNORE_ALL);

            List<DiffEntry> listDiffs = diffFormatter.scan(oldTreeIter, newTreeIter);

            int counter = 0;

            for(DiffEntry diff: listDiffs){

                diffFormatter.format(diff);

                Object[] objArr = new Object[8];
                objArr[0] = getCurrentDateTime();
                objArr[1] = repository.resolve(Constants.HEAD).getName().substring(0, 7);
                try(RevWalk walk = new RevWalk(repository)) {
                    RevCommit commit = walk.parseCommit(repository.resolve(Constants.HEAD));
                    objArr[2] = commit.getFullMessage();
                    walk.dispose();
                }

                objArr[3] = repositoryBuilder.getWorkTree().getPath();
                objArr[4] = diff.getChangeType().name().equals("DELETE") ? diff.getOldPath() : diff.getNewPath();
                objArr[5] = diff.getChangeType();
                // The differences are accumulated in ByteArrayOutputStream. So each time we get the last one.
                String[] diffScripts = out.toString().split("(?=diff)");
                // Tab is not kept when written on excel file. So putting spaces instead.
                if(counter < 9) objArr[6] = diffScripts[diffScripts.length - 1].replace("\t", "    ");

                data.put(counter++, objArr);
            }



        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }



}

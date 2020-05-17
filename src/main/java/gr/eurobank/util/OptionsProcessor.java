package gr.eurobank.util;

import org.apache.commons.cli.*;

public class OptionsProcessor {

    private CommandLineParser cmdParser;
    private HelpFormatter formatter;
    private String[] args;
    private Options options;
    private OptionsProcessor.CmdData cmdData;

    public class CmdData{
        private String directory;

        public String getDirectory() {
            return directory;
        }

        public void setDirectory(String directory) {
            this.directory = directory;
        }
    }

    public OptionsProcessor(String[] args) {
        options = new Options();
        options.addOption("d", "directory", true, "Obligatory. The root directory of the git repository.");
        cmdParser = new DefaultParser();
        this.args = args;
        formatter = new HelpFormatter();
    }

    public CmdData processOptions() throws ParseException {

        CommandLine cmd = null;

        //First check
        try{
            cmd = cmdParser.parse(options, args);
        } catch (UnrecognizedOptionException e){
            throw new RuntimeException("Invalid Option. Please enter --directory or -d");
        }

        //Second check
        if(cmd.getOptions().length == 0) {
            formatter.printHelp("CommitHandler", "",options, "\nex: CommitHandler -d ebankinguseradminimpl\n   CommitHandler --directory ebankinguseradminimpl",
                    true);
            System.exit(-1);
        }

        cmdData = this.new CmdData();
        cmdData.directory = cmd.getOptionValue("directory");

        return cmdData;
    }
}

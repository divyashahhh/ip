package cs2103;


public class Parser {
    /**
     * Parses user input strings into intent + arguments.
     * returns a ParsedCommand that Paneer uses to execute.
     */

        public static ParsedCommand parse(String input) throws PaneerException {
            assert input != null : "Parser.parse received null input";
            if (input.isEmpty()) {
                throw new PaneerException("Who do you think I am? Tofu? Feed me a command to cook!");
            }

            String[] tokens = input.split(" ", 2);
            assert tokens.length >= 1 : "Tokenizer should yield at least one token";
            assert tokens[0] != null && !tokens[0].isBlank() : "Command word should not be blank";
            String command = tokens[0];
            String rest = (tokens.length > 1) ? tokens[1].trim() : "";

            switch (command.toLowerCase()) {
                case "bye":
                    return ParsedCommand.exit();
                case "list":
                    return ParsedCommand.list();
                case "undo":
                    return new ParsedCommand(ParsedCommand.Type.UNDO, null, null, null, null);
                case "edit": {
                    // Syntax:
                    // edit <index> desc:<text> [by:<date>] [from:<dt>] [to:<dt>]
                    String[] first = rest.split(" ", 2);
                    if (first.length == 0 || first[0].isBlank()) {
                        throw new PaneerException("Tell Paneer which task to edit: edit <index> ...");
                    }
                    int idx = parseIndex(first[0]);
                    String args = (first.length > 1) ? first[1].trim() : "";
                    if (args.isEmpty()) {
                        // Interactive edit: just index, Paneer will prompt for replacement
                        return new ParsedCommand(ParsedCommand.Type.EDIT, idx, null, null, null);
                    }
                    String desc = extractArg(args, "desc:");
                    String by = extractArg(args, "by:");
                    String from = extractArg(args, "from:");
                    String to = extractArg(args, "to:");
                    if (desc == null && by == null && from == null && to == null) {
                        throw new PaneerException("No valid fields found. Use desc:, by:, from:, to: (don’t skimp on the masala)");
                    }
                    ParsedCommand pc = new ParsedCommand(ParsedCommand.Type.EDIT, idx, desc, from, to);
                    pc.when2 = by; // reuse when2 to carry 'by' (or to) depending on type
                    return pc;
                }
                case "mark":
                    return ParsedCommand.mark(parseIndex(rest));
                case "unmark":
                    return ParsedCommand.unmark(parseIndex(rest));
                case "delete":
                    return ParsedCommand.delete(parseIndex(rest));
                case "todo":
                    if (rest.isEmpty()) {
                        throw new PaneerException("I have no idea what you mean, try again — add some flavour.");
                    }
                    return ParsedCommand.addTodo(rest);
                case "find":
                    if (rest.isEmpty()) {
                        throw new PaneerException("Tell Paneer what to find, e.g., find book — what’s cooking?");
                    }
                    return ParsedCommand.find(rest);

                    case "deadline": {
                    String[] deadlineParts = rest.split(" /by ", 2);
                    if (deadlineParts.length < 2 || deadlineParts[0].trim().isEmpty() || deadlineParts[1].trim().isEmpty()) {
                        throw new PaneerException(
                                "I can't decipher those timings! Use: deadline <desc> /by yyyy-MM-dd (e.g., 2019-12-02)");
                    }
                    return ParsedCommand.addDeadline(deadlineParts[0].trim(), deadlineParts[1].trim());
                }
                case "event": {
                    String[] fromParts = rest.split(" /from ", 2);
                    if (fromParts.length < 2 || fromParts[0].trim().isEmpty()) {
                        throw new PaneerException(
                                "I can't decipher those timings! Use: event <desc> /from yyyy-MM-dd HHmm /to yyyy-MM-dd HHmm");
                    }
                    String desc = fromParts[0].trim();
                    String[] toParts = fromParts[1].split(" /to ", 2);
                    if (toParts.length < 2 || toParts[0].trim().isEmpty() || toParts[1].trim().isEmpty()) {
                        throw new PaneerException(
                                "Paneer needs both start and end times. Use: event <desc> /from yyyy-MM-dd HHmm /to yyyy-MM-dd HHmm");
                    }
                    return ParsedCommand.addEvent(desc, toParts[0].trim(), toParts[1].trim());
                }
                case "sort":
                    return new ParsedCommand(ParsedCommand.Type.SORT, null, null, null, null);
                default:
                    throw new PaneerException("Paneer can't tell what that means! Try: list, todo, deadline, event, mark, unmark, delete, bye. (or ask for a tasting menu)");
            }
        }

        private static int parseIndex(String s) throws PaneerException {
            try {
                int oneBased = Integer.parseInt(s);
                int idx = oneBased - 1;
                if (idx < 0) {
                    throw new IndexOutOfBoundsException();
                }
                return idx;
            } catch (NumberFormatException e) {
                throw new PaneerException("Paneer is not that smart, that number does not seem right... try again!");
            } catch (IndexOutOfBoundsException e) {
                throw new PaneerException("That task number is out of Paneer's capabilities :( Try again!");
            }
        }


        public static class ParsedCommand {
            public enum Type { EXIT, LIST, MARK, UNMARK, DELETE, ADD_TODO, ADD_DEADLINE, ADD_EVENT, FIND, SORT, UNDO, EDIT }

            public final Type type;
            public final Integer index;     // for mark/unmark/delete
            public final String desc;       // for add
            public final String when1;      // deadline date OR event start
            public String when2;      // event end or deadline by

            private ParsedCommand(Type type, Integer index, String desc, String when1, String when2) {
                this.type = type;
                this.index = index;
                this.desc = desc;
                this.when1 = when1;
                this.when2 = when2;
            }

            public static ParsedCommand exit() { return new ParsedCommand(Type.EXIT, null, null, null, null); }
            public static ParsedCommand list() { return new ParsedCommand(Type.LIST, null, null, null, null); }
            public static ParsedCommand mark(int idx) { return new ParsedCommand(Type.MARK, idx, null, null, null); }
            public static ParsedCommand unmark(int idx) { return new ParsedCommand(Type.UNMARK, idx, null, null, null); }
            public static ParsedCommand delete(int idx) { return new ParsedCommand(Type.DELETE, idx, null, null, null); }
            public static ParsedCommand addTodo(String desc) { return new ParsedCommand(Type.ADD_TODO, null, desc, null, null); }
            public static ParsedCommand addDeadline(String desc, String byRaw) { return new ParsedCommand(Type.ADD_DEADLINE, null, desc, byRaw, null); }
            public static ParsedCommand addEvent(String desc, String startRaw, String endRaw) { return new ParsedCommand(Type.ADD_EVENT, null, desc, startRaw, endRaw); }
            public static ParsedCommand find(String keyword) { return new ParsedCommand(Type.FIND, null, keyword, null, null); }

        }

        private static String extractArg(String args, String key) {
            int i = args.indexOf(key);
            if (i < 0) return null;
            int start = i + key.length();
            int end = args.length();
            // find next key occurrence to limit range
            for (String k : new String[]{" desc:", " by:", " from:", " to:"}) {
                int j = args.indexOf(k, start);
                if (j >= 0 && j < end) end = j;
            }
            return args.substring(start, end).trim();
        }
}



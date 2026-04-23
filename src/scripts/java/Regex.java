public class Regex {
    public static void main(String[] args) {
        System.out.println("true: " + isInvalidIPs("1.1.1.1"));
        System.out.println("true: " + isInvalidIPs("1.1.1.1, 2.2.2.2,3.3.3.3"));
        System.out.println("false: " + isInvalidIPs("1.1.256.1"));
        System.out.println("true: " + isInvalidIPs(""));
        
        System.out.println("true: " + isInvalidMacs("AA:BB:CC:DD:EE:FF"));
        System.out.println("true: " + isInvalidMacs("AA:BB:CC:DD:EE:FF,11:22:33:44:55:66, 11-22-33-44-55-66"));
        System.out.println("false: " + isInvalidMacs("AA:BB:CC:DD:EE"));
        System.out.println("false: " + isInvalidMacs("AA:BB-CC:DD:EE:FF"));
        System.out.println("false: " + isInvalidMacs("GG:HH:II:JJ:KK:LL"));
        System.out.println("true: " + isInvalidMacs(""));
    }
    
    private static boolean isInvalidIPs(String ipSequence) {
        String regex = "^\\s*$|^(?:25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]\\d?|0)" +
                "(?:\\.(?:25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]\\d?|0)){3}" +
                "(?:\\s*,\\s*(?:25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]\\d?|0)" +
                "(?:\\.(?:25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]\\d?|0)){3})*$";
        
        return ipSequence.matches(regex);
    }
    
    private static boolean isInvalidMacs(String macSequence) {
        String regex = "^(?:\\s*$|[0-9A-Fa-f]{2}" +
                "([-:])(?:[0-9A-Fa-f]{2}\\1){4}" +
                "[0-9A-Fa-f]{2}(?:\\s*,\\s*[0-9A-Fa-f]{2}" +
                "([-:])(?:[0-9A-Fa-f]{2}\\2){4}[0-9A-Fa-f]{2})*$)";
        
        return macSequence.matches(regex);
    }
}

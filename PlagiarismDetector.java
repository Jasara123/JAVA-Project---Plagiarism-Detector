package MainCode;

public class PlagiarismDetector {
    private String result;
    private String[][] data = new String[2][];


    public PlagiarismDetector(String[][] data) {
        this.data = data;
    }

    public void lcs() {

        int m = data[0].length;
        int n = data[1].length;

        int[][] dp = new int[m + 1][n + 1];

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (data[0][i-1].equals(data[1][j-1])) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        

        StringBuilder result1 = new StringBuilder();
        int i = m, j = n;
        while (i > 0 && j > 0) {
            if (data[0][i-1].equals(data[1][j-1])) {
                result1.append(data[0][i - 1]).append(" ");
                i--;
                j--;
            } else if (dp[i - 1][j] >= dp[i][j - 1]) {
                i--;
            } else {
                j--;
            }
        }
        result = result1.reverse().toString();
    }

    public String getResult() {
        return result;
    }
}


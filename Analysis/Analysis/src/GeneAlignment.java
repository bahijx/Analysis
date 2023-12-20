import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class GeneAlignment {

    public static void main(String[] args) {
        String x = "ATGCC";
        String y = "TACGCA";

        Map<Character, Map<Character, Double>> scoringMatrix = new HashMap<>();
        scoringMatrix.put('A', Map.of('A', 1.0, 'G', -0.8, 'T', -0.2, 'C', -2.3, '-', -0.6));
        scoringMatrix.put('G', Map.of('A', -0.8, 'G', 1.0, 'T', -1.1, 'C', -0.7, '-', -1.5));
        scoringMatrix.put('T', Map.of('A', -0.2, 'G', -1.1, 'T', 1.0, 'C', -0.5, '-', -0.9));
        scoringMatrix.put('C', Map.of('A', -2.3, 'G', -0.7, 'T', -0.5, 'C', 1.0, '-', -1.0));
        scoringMatrix.put('-', Map.of('A', -0.6, 'G', -1.5, 'T', -0.9, 'C', -1.0, '-', -100000000.0));

        String[] result = highestScoringAlignment(x, y, scoringMatrix);
        System.out.println("Alignment x: " + result[0]);
        System.out.println("Alignment y: " + result[1]);

        double score = scoreAlignment(result[0], result[1], scoringMatrix);
        System.out.println("Score: " + score);
    }

    public static String[] highestScoringAlignment(String x, String y,
            Map<Character, Map<Character, Double>> scoringMatrix) {
        int n = x.length();
        int m = y.length();

        double[][] dp = new double[n + 1][m + 1];

        for (int i = 1; i <= n; i++) {
            dp[i][0] = dp[i - 1][0] + scoringMatrix.get(x.charAt(i - 1)).get('-');
        }

        for (int j = 1; j <= m; j++) {
            dp[0][j] = dp[0][j - 1] + scoringMatrix.get('-').get(y.charAt(j - 1));
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                double matchScore = dp[i - 1][j - 1] + scoringMatrix.get(x.charAt(i - 1)).get(y.charAt(j - 1));
                double deleteScore = dp[i - 1][j] + scoringMatrix.get(x.charAt(i - 1)).get('-');
                double insertScore = dp[i][j - 1] + scoringMatrix.get('-').get(y.charAt(j - 1));
                dp[i][j] = Math.max(matchScore, Math.max(deleteScore, insertScore));
            }
        }

        int i = n;
        int j = m;
        StringBuilder alignmentX = new StringBuilder();
        StringBuilder alignmentY = new StringBuilder();

        while (i > 0 || j > 0) {
            if (i > 0 && j > 0
                    && dp[i][j] == dp[i - 1][j - 1] + scoringMatrix.get(x.charAt(i - 1)).get(y.charAt(j - 1))) {
                alignmentX.insert(0, x.charAt(i - 1));
                alignmentY.insert(0, y.charAt(j - 1));
                i--;
                j--;
            } else if (i > 0 && dp[i][j] == dp[i - 1][j] + scoringMatrix.get(x.charAt(i - 1)).get('-')) {
                alignmentX.insert(0, x.charAt(i - 1));
                alignmentY.insert(0, '-');
                i--;
            } else {
                alignmentX.insert(0, '-');
                alignmentY.insert(0, y.charAt(j - 1));
                j--;
            }
        }

        return new String[] { alignmentX.toString(), alignmentY.toString() };
    }

    public static double scoreAlignment(String x, String y, Map<Character, Map<Character, Double>> scoringMatrix) {
        BigDecimal score = BigDecimal.ZERO;

        for (int i = 0; i < x.length(); i++) {
            char charX = x.charAt(i);
            char charY = y.charAt(i);
            double matrixValue = scoringMatrix.get(charX).get(charY);
            score = score.add(BigDecimal.valueOf(matrixValue));
        }

        return score.doubleValue();
    }
}

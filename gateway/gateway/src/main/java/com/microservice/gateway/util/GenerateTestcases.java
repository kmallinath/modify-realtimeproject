package com.microservice.gateway.util;

import java.io.*;
import java.util.*;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class GenerateTestcases {
    public static long maxGoldWeight(long[] A, int n) {
        Arrays.sort(A);
        long maxWeight = 0;
        for (int i = n - 1; i >= 0; i--) {
            long value = (long) (n - i) * A[i];
            if (value > maxWeight) maxWeight = value;
        }
        return maxWeight;
    }

    private static JSONObject makeCase(long[] arr, int order) {
        int n = arr.length;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(arr[i]);
            if (i < n - 1) sb.append(" ");
        }
        JSONObject obj = new JSONObject(new LinkedHashMap<>());
        // ✅ Removed trailing newline from input and output
        obj.put("input", n + "\n" + sb.toString());
        obj.put("output", String.valueOf(maxGoldWeight(arr.clone(), n)));
        obj.put("order", order);
        return obj;
    }

    public static void main(String[] args) throws Exception {
        List<JSONObject> testcases = new ArrayList<>();
        Random rand = new Random();
        int order = 1;

        // 10 edge cases
        long[][] edgeCases = {
                {5}, {1,1,1,1,1}, {1,2,3,4,5}, {5,4,3,2,1}, {1,2,2,3,4},
                {1000000000L,1000000000L,1000000000L}, {1,1000000000L}, {999999999L,500000000L,1L},
                {1,1,1000000000L,1000000000L}, {10,9,9,9,1}
        };
        for (long[] arr : edgeCases) testcases.add(makeCase(arr, order++));

        // 30 small
        for (int t=0; t<30; t++){
            int n = rand.nextInt(100)+1;
            long[] arr = new long[n];
            for (int i=0;i<n;i++) arr[i]=1L+(long)(rand.nextDouble()*1000);
            shuffle(arr, rand);
            testcases.add(makeCase(arr, order++));
        }

        // 40 medium
        for (int t=0; t<40; t++){
            int n = 100 + rand.nextInt(900);
            long[] arr = new long[n];
            for (int i=0;i<n;i++) arr[i]=1L+(long)(rand.nextDouble()*100000);
            shuffle(arr, rand);
            testcases.add(makeCase(arr, order++));
        }

        // 20 large
        for (int t=0; t<20; t++){
            int n = 1000 + rand.nextInt(9000);
            long[] arr = new long[n];
            for (int i=0;i<n;i++) arr[i]=1L+(long)(rand.nextDouble()*1000000000L);
            shuffle(arr, rand);
            testcases.add(makeCase(arr, order++));
        }

        // 5 very large
        for (int t=0; t<5; t++){
            int n = 100000;
            long[] arr = new long[n];
            for (int i=0;i<n;i++) arr[i]=1L+(long)(rand.nextDouble()*1000000000L);
            shuffle(arr, rand);
            testcases.add(makeCase(arr, order++));
        }

        // Write JSON manually with proper format
        try (FileWriter file = new FileWriter("testcases.json")) {
            file.write("[\n");
            for (int i = 0; i < testcases.size(); i++) {
                JSONObject obj = testcases.get(i);
                String input = (String)obj.get("input");
                String output = (String)obj.get("output");

                file.write("  {\n");
                file.write("    \"input\": \"" + escapeJson(input) + "\",\n");
                file.write("    \"output\": \"" + escapeJson(output) + "\",\n");
                file.write("    \"order\": " + obj.get("order") + "\n");
                file.write("  }" + (i < testcases.size() - 1 ? ",\n" : "\n"));
            }
            file.write("]\n");
        }

        System.out.println("✅ Successfully generated " + (order - 1) + " test cases with correct field order in testcases.json");
    }

    private static void shuffle(long[] arr, Random rand) {
        for (int i = arr.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            long tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp;
        }
    }

    private static String escapeJson(String s) {
        if (s == null) return "";
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"': sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default: sb.append(c);
            }
        }
        return sb.toString();
    }
}

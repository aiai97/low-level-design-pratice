package org.fulfillrouter.lowleveldesignpractice.lowleveldesign.ratelimiter;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public final class RateLimiterProblem {

    /*
     * Problem: Design a Sliding Window Rate Limiter.
     *
     * Requirements:
     * 1) Limit request count per key in a time window.
     * 2) Reject requests when the key exceeds quota.
     * 3) Expire outdated request timestamps automatically.
     * 4) Support deterministic testing with explicit timestamps.
     * 5) Keep operations efficient and thread-safe for shared use.
     */

    private RateLimiterProblem() {
    }

    public static final class SlidingWindowRateLimiter {
        private final int maxRequests;
        private final long windowMillis;
        private final Map<String, Deque<Long>> requestTimes = new HashMap<>();

        public SlidingWindowRateLimiter(int maxRequests, long windowMillis) {
            if (maxRequests <= 0 || windowMillis <= 0) {
                throw new IllegalArgumentException("maxRequests and windowMillis must be > 0");
            }
            this.maxRequests = maxRequests;
            this.windowMillis = windowMillis;
        }

        public synchronized boolean allow(String key) {
            return allow(key, System.currentTimeMillis());
        }

        public synchronized boolean allow(String key, long nowMillis) {
            Deque<Long> queue = requestTimes.computeIfAbsent(key, ignored -> new ArrayDeque<>());
            long threshold = nowMillis - windowMillis;
            while (!queue.isEmpty() && queue.peekFirst() <= threshold) {
                queue.pollFirst();
            }
            if (queue.size() >= maxRequests) {
                return false;
            }
            queue.offerLast(nowMillis);
            return true;
        }
    }

    public static String runDemo() {
        SlidingWindowRateLimiter limiter = new SlidingWindowRateLimiter(3, 1000);
        boolean r1 = limiter.allow("client-A", 0);
        boolean r2 = limiter.allow("client-A", 100);
        boolean r3 = limiter.allow("client-A", 200);
        boolean r4 = limiter.allow("client-A", 300);
        boolean r5 = limiter.allow("client-A", 1201);

        return "RateLimiter: " + r1 + "," + r2 + "," + r3 + "," + r4 + "," + r5;
    }
}



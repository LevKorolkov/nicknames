import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static final AtomicInteger counter1 = new AtomicInteger();
    public static final AtomicInteger counter2 = new AtomicInteger();
    public static final AtomicInteger counter3 = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        Thread palindrome = new Thread(() -> {
            for (String text : texts) {
                if (isPalindrome(text) && !hasSameChars(text)) {
                    incrementCounter(text.length());
                }
            }
        });
        palindrome.start();

        Thread sameChar = new Thread(() -> {
            for (String text : texts) {
                if (hasSameChars(text)) {
                    incrementCounter(text.length());
                }
            }
        });
        sameChar.start();

        Thread ascendingOrder = new Thread(() -> {
            for (String text : texts) {
                if (!isPalindrome(text) && isOrderAsc(text)) {
                    incrementCounter(text.length());
                }
            }
        });
        ascendingOrder.start();

        sameChar.join();
        ascendingOrder.join();
        palindrome.join();

        System.out.println("Красивых слов с длиной 3: " + counter1);
        System.out.println("Красивых слов с длиной 4: " + counter2);
        System.out.println("Красивых слов с длиной 5: " + counter3);
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static boolean isPalindrome(String text) {
        return text.equals(new StringBuilder(text).reverse().toString());
    }

    public static boolean hasSameChars(String text) {
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i) != text.charAt(i - 1))
                return false;
        }
        return true;
    }

    public static boolean isOrderAsc(String text) {
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i) < text.charAt(i - 1))
                return false;
        }
        return true;
    }

    public static void incrementCounter(int textLength) {
        if (textLength == 3) {
            counter1.getAndIncrement();
        } else if (textLength == 4) {
            counter2.getAndIncrement();
        } else {
            counter3.getAndIncrement();
        }
    }
}

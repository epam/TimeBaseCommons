package deltix.util.collections;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Test_ByteQueue {

    /**
     * Reproduces logic from {@link deltix.util.vsocket.VSocketOutputStream#dumpInternal}
     */
    @Test
    void testGrowForVSocketOutputStream() {
        ByteQueue queue = new ByteQueue(524288);
        byte[] arr1 = new byte[524288];
        queue.offer(arr1, 0, arr1.length);

        byte[] arr2 = new byte[2];

        int overflow = queue.size() + arr2.length - queue.capacity();
        if (overflow > 0) {
            int increment = 1024 * 512 / 4;
            int incrementsToAdd = divideRoundUp(overflow, increment);
            queue.addCapacity(increment * incrementsToAdd);
        }

        queue.offer(arr2, 0, arr2.length);
        assertEquals(524290, queue.size());
    }


    static int divideRoundUp(int val, int divisor) {
        int result = val / divisor;
        if (val > result * divisor) {
            result += 1;
        }
        return result;
    }

    // region Generated Tests

    @Test
    void testOfferArrayContiguous() {
        // Setup: Capacity 10
        ByteQueue queue = new ByteQueue(10);

        byte[] input = new byte[] { 1, 2, 3, 4, 5 };

        // Act: Offer 5 bytes (fits comfortably at start)
        // Branches: excess <= 0, tail < capacity
        queue.offer(input, 0, 5);

        // Assert
        assertEquals(5, queue.size());
        assertEquals(0, queue.getHead());
        assertEquals(5, queue.getTail());
        assertEquals(1, queue.get(0));
        assertEquals(5, queue.get(4));
    }

    @Test
    void testOfferArrayWrapAround() {
        ByteQueue queue = new ByteQueue(10);

        // Move tail to index 8 (fill 8 bytes)
        queue.offer(new byte[8], 0, 8);
        // Move head to index 4 (poll 4 bytes) -> Size is now 4, Tail is 8
        queue.poll(new byte[4], 0, 4);

        assertEquals(4, queue.size());
        assertEquals(8, queue.getTail());

        // Act: Offer 4 bytes
        // Logic: tail(8) + length(4) = 12. Capacity is 10.
        // excess = 2. This triggers the "if (excess > 0)" split branch.
        byte[] input = new byte[] { 10, 11, 12, 13 };
        queue.offer(input, 0, 4);

        // Assert
        assertEquals(8, queue.size()); // 4 existing + 4 new
        assertEquals(2, queue.getTail()); // Wrapped: (8 + 4) % 10 = 2

        // Verify data integrity across wrap
        // Index 0 in queue (logical) is at buffer[4]
        // New data starts at logical index 4
        assertEquals(10, queue.get(4));
        assertEquals(11, queue.get(5));
        assertEquals(12, queue.get(6));
        assertEquals(13, queue.get(7));
    }

    @Test
    void testOfferArrayExactFitAtEnd() {
        ByteQueue queue = new ByteQueue(10);

        queue.offer(new byte[5], 0, 5);

        // Act: Offer exactly 5 bytes to fill the buffer to the max capacity.
        // Logic: tail(5) + length(5) = 10. excess = 0.
        byte[] input = new byte[] { 1, 2, 3, 4, 5 };
        queue.offer(input, 0, 5);

        assertEquals(10, queue.size());
        assertEquals(0, queue.getTail()); // Should wrap exactly to 0
        assertTrue(queue.isFull());

        queue.skip(2);
        assertEquals(8, queue.size());

        queue.offer(new byte[] {6, 7}, 0, 2);
        assertEquals(10, queue.size());
    }

    @Test
    void testSkipSimple() {
        ByteQueue queue = new ByteQueue(10);
        queue.offer(new byte[] { 1, 2, 3, 4, 5 }, 0, 5);

        // Act: Skip 2 bytes
        // Branches: head + length < capacity
        queue.skip(2);

        // Assert
        assertEquals(3, queue.size());
        assertEquals(2, queue.getHead()); // Head moved from 0 to 2
        assertEquals(3, queue.get(0)); // Logical 0 is now the old 2
    }

    @Test
    void testSkipWrapAround() {
        ByteQueue queue = new ByteQueue(10);

        // Arrange: Wrap the data. Tail at 2, Head at 8. Size 4.
        // We do this by filling 10, then polling 8, then offering 2.
        queue.offer(new byte[10], 0, 10);
        queue.poll(new byte[8], 0, 8); // Head is now 8
        queue.offer(new byte[] { 99, 100 }, 0, 2); // Tail is now 2

        assertEquals(8, queue.getHead());
        assertEquals(4, queue.size());

        // Act: Skip 3 bytes.
        // Logic: Head(8) + 3 = 11. Capacity 10.
        // Branch: if (head >= capacity) head -= capacity
        queue.skip(3);

        // Assert
        assertEquals(1, queue.size()); // 4 - 3 = 1 left
        assertEquals(1, queue.getHead()); // (8 + 3) - 10 = 1
    }

    @Test
    void testSetCapacityIncreaseContiguous() {
        ByteQueue queue = new ByteQueue(5);
        queue.offer(new byte[] { 1, 2, 3 }, 0, 3);

        // Act: Increase capacity
        // Branch: tail > head (3 > 0)
        boolean changed = queue.setCapacity(10);

        // Assert
        assertTrue(changed);
        assertEquals(10, queue.capacity());
        assertEquals(3, queue.size());
        assertEquals(1, queue.get(0)); // Data preserved

        // Verify we can actually use the new space
        queue.offer(new byte[6], 0, 6); // Should fit (3 + 6 = 9 <= 10)
        assertEquals(9, queue.size());
    }

    @Test
    void testSetCapacityIncreaseWrapped() {
        ByteQueue queue = new ByteQueue(5);
        // Wrap: Head 3, Tail 2, Size 4
        queue.offer(new byte[5], 0, 5); // Full
        queue.poll(new byte[3], 0, 3); // Head 3, Size 2
        queue.offer(new byte[] { 10, 11 }, 0, 2); // Tail 2, Size 4

        assertEquals(3, queue.getHead());
        assertEquals(2, queue.getTail());

        // Act: Increase capacity to 10
        // Branch: tail <= head (2 <= 3). Must linearize buffer.
        queue.setCapacity(10);

        // Assert
        assertEquals(10, queue.capacity());
        assertEquals(4, queue.size());
        assertEquals(0, queue.getHead()); // Linearized -> Head 0
        assertEquals(4, queue.getTail()); // Linearized -> Tail 4

        // Verify internal data consistency
        // Old buffer logical order: [index 3, index 4, index 0, index 1]
        // Logic values should be preserved
        // We pushed 5 empty bytes, then pulled 3. Remaining: 2 empty bytes.
        // Then pushed 10, 11.
        // Queue content: [0, 0, 10, 11]
        assertEquals(0, queue.get(0));
        assertEquals(0, queue.get(1));
        assertEquals(10, queue.get(2));
        assertEquals(11, queue.get(3));
    }

    @Test
    void testSetCapacityResizesBufferCorrectly() {
        ByteQueue queue = new ByteQueue(10);
        queue.setCapacity(20);

        // Assertion 1: Variable is updated
        assertEquals(20, queue.capacity());

        // Assertion 2: Internal buffer is actually updated
        // We can check this by filling it beyond the old capacity
        byte[] largeData = new byte[15];
        Arrays.fill(largeData, (byte) 1);

        // If buffer was still size 10, this would throw ArrayIndexOutOfBoundsException
        assertDoesNotThrow(() -> {
            queue.offer(largeData, 0, 15);
        }, "Buffer should be resized to accommodate new capacity");

        assertEquals(15, queue.size());
    }

    // endregion
}

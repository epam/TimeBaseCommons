package deltix.qsrv.hf.pub;

import java.util.HashMap;
import java.util.Map;

public enum HaltReason {

    NewsDissemination(0, 0, 'D'),
    OrderInflux(0, 1, 'E'),
    OrderImbalance(0, 2, 'I'),
    AdditionalInformation(0, 3, 'M'),
    NewPending(0, 4, 'P'),
    EquipmentChangeover(0, 5, 'X'),
    
    SuspendedBySurveillance(1, 1),
    Trading(1, 2),
    InstrumentAuthorization(1, 3),
    ReturnToNormalState(1, 4),
    
    GroupSchedule(2, 0),
    SurveillanceIntervention(2, 1),
    MarketEvent(2, 2),
    InstrumentActivation(2, 3),
    InstrumentExpiration(2, 4),
    Unknown(2, 5);
 
    private static final HaltReason[][] REASONS;

    static {
        int maxNumFix1 = Integer.MIN_VALUE;
        int maxNumFix2 = Integer.MIN_VALUE;
        int maxNumFix3 = Integer.MIN_VALUE;
        
        final Map<Integer, HaltReason> fix1 = new HashMap<>();
        final Map<Integer, HaltReason> fix2 = new HashMap<>();
        final Map<Integer, HaltReason> fix3 = new HashMap<>();
        
        for (HaltReason t : HaltReason.values()) {
            final int[] nums = t.getValue();
            final int fixNumber = nums[0];
            for (int i = 1, iCount = nums.length; i < iCount; i++) {

                final int num = nums[i];   
                switch (fixNumber) {
                    case 0:
                        if (num > maxNumFix1) {
                            maxNumFix1 = num;
                        }
                        fix1.put(num, t);
                        break;
                    case 1:
                        if (num > maxNumFix2) {
                            maxNumFix2 = num;
                        }                
                        fix2.put(num, t);
                        break;
                    case 2:
                        if (num > maxNumFix3) {
                            maxNumFix3 = num;
                        }   
                        fix3.put(num, t);
                        break;
                }
            }
        }
        
        REASONS = new HaltReason[3][];
        REASONS[0] = new HaltReason[maxNumFix1 + 1];
        REASONS[1] = new HaltReason[maxNumFix2 + 1];
        REASONS[2] = new HaltReason[maxNumFix3 + 1];
        
        for (Map.Entry<Integer, HaltReason> ct : fix1.entrySet()) {
            REASONS[0][ct.getKey()] = ct.getValue();
        }
        for (Map.Entry<Integer, HaltReason> ct : fix2.entrySet()) {
            REASONS[1][ct.getKey()] = ct.getValue();
        }
        for (Map.Entry<Integer, HaltReason> ct : fix3.entrySet()) {
            REASONS[2][ct.getKey()] = ct.getValue();
        }        
    }
    
    public static HaltReason fromNumber(int fixId, int number) {
        if (fixId < 0 || fixId > 2) {
            throw new IllegalArgumentException("Fix ID " + fixId + " is out of the range [0, 3]");
        }
        
        if (number < 0 || number > REASONS[fixId].length) {
            throw new IllegalArgumentException("Number " + number + " is out of the range [0, " + REASONS[fixId].length + "].");
        }
        
        return REASONS[fixId][number];
    }
    
    private final int[]                   numbers;

    private HaltReason(int... number) {
        this.numbers = number;        
    }

    public int[] getValue() {
        return numbers;
    }            
}

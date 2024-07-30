package frc.robot.subsystems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.LEDConsts;
import frc.robot.subsystems.LEDSubsystem.Range.InheritMode;

public class LEDSubsystem extends SubsystemBase {

    public static abstract class Animation {
        protected long timer = 0;
        protected Range range;

        public abstract void update(LEDSubsystem sub);
        public abstract Animation[] withRanges(Range... ranges);

        public void resetTimer() {
            timer = 0;
        }
        public void incTimer() {
            timer++;
        }
        public Animation withSync(Animation a) {
            timer = a.timer();
            return this;
        }
        
        public Range range() {return range;}
        public long timer() {return timer;}
    }
    public static class RainbowAnim extends Animation {

        public static final double DEF_SPEED = 1;
        public static final int DEF_SATURATION = 255;
        public static final int DEF_VALUE = 25;

        public double speed;
        public int saturation, value;

        public RainbowAnim() {
            this(null);
        }
        public RainbowAnim(double _speed, int _saturation, int _value) {
            this(null, _speed, _saturation, _value);
        }
        private RainbowAnim(Range _range) {
            this(_range, DEF_SPEED, DEF_SATURATION, DEF_VALUE);
        }
        private RainbowAnim(Range _range, double _speed, int _saturation, int _value) {
            range = _range;
            speed = _speed;
            saturation = _saturation;
            value = _value;
        }

        @Override
        public void update(LEDSubsystem sub) {
            final double cSpeed = Math.max(speed, 0);
            final int cSaturation = Math.min(Math.max(saturation, 0), 255);
            final int cValue = Math.min(Math.max(value, 0), 255);

            Color calc = Color.fromHSV((int) (timer * cSpeed) % 180, cSaturation, cValue);
            for(int i = 0; i < range.length(); i++) {
                sub.setPixel(range, i, calc);
            }
        }
        @Override
        public RainbowAnim[] withRanges(Range... ranges) {
            RainbowAnim[] copies = new RainbowAnim[ranges.length];
            for(int i = 0; i < ranges.length; i++) {
                copies[i] = new RainbowAnim(ranges[i], speed, saturation, value);
            }

            return copies;
        }
    }
    public static class ChaseAnim extends Animation {

        public static final double DEF_SPEED = 0.6;
        public static final int DEF_SEGMENTLEN = 20;
        public static final int DEF_SPACING = 0;
        public static final boolean DEF_USEGLOBALINDEX = false;

        public Color col1, col2;
        public double speed;
        public int segmentLen, spacing;
        public boolean useGlobalIndex;

        public ChaseAnim(Color _col1, Color _col2) {
            this(null, _col1, _col2);
        }
        public ChaseAnim(Color _col1, Color _col2, double _speed, int _segmentLen, int _spacing, boolean _useGlobalIndex) {
            this(null, _col1, _col2, _speed, _segmentLen, _spacing, _useGlobalIndex);
        }
        private ChaseAnim(Range _range, Color _col1, Color _col2) {
            this(_range, _col1, _col2, DEF_SPEED, DEF_SEGMENTLEN, DEF_SPACING, DEF_USEGLOBALINDEX);
        }
        private ChaseAnim(Range _range, Color _col1, Color _col2, double _speed, int _segmentLen, int _spacing, boolean _useGlobalIndex) {
            range = _range;
            col1 = _col1;
            col2 = _col2;
            speed = _speed;
            segmentLen = _segmentLen;
            spacing = _spacing;
            useGlobalIndex = _useGlobalIndex;
        }
        
        @Override
        public void update(LEDSubsystem sub) {
            final double cSpeed = Math.max(speed, 0);
            final int cSegmentLen = Math.max(segmentLen, 1);
            final int cSpacing = Math.max(spacing, 0);

            for(int i = 0; i < range.length(); i++) {
                int modI = i;
                if(useGlobalIndex) modI = (range.inheritMode() == InheritMode.MIRROR ? range.end() - i : range.start() + i);

                double loc = (modI + timer * cSpeed) / (cSegmentLen + cSpacing) % 2;

                Color col;
                if(loc % 1 >= 1 - cSpacing / (cSegmentLen + cSpacing)) col = Color.kBlack;
                else if(Math.floor(loc) == 0) col = col1;
                else col = col2;

                sub.setPixel(range, i, col);
            }
        }
        @Override
        public ChaseAnim[] withRanges(Range... ranges) {
            ChaseAnim[] copies = new ChaseAnim[ranges.length];
            for(int i = 0; i < ranges.length; i++) {
                copies[i] = new ChaseAnim(ranges[i], col1, col2, speed, segmentLen, spacing, useGlobalIndex);
            }

            return copies;
        }
    }
    public static class BlinkAnim extends Animation {

        public static final int DEF_ONTIME = 25;
        public static final int DEF_OFFTIME = 25;

        public Color col;
        public int onTime, offTime;

        public BlinkAnim(Color _col) {
            this(null, _col);
        }
        /**
         * *Note* On and off time is measured in frames. With a update rate of 50 Hz, 50
         * frames is 1
         * second, 25 frames is 0.5 seconds, etc.
         */
        public BlinkAnim(Color _col, int _onTime, int _offTime) {
            this(null, _col, _onTime, _offTime);
        }
        private BlinkAnim(Range _range, Color _col) {
            this(_range, _col, DEF_ONTIME, DEF_OFFTIME);
        }
        private BlinkAnim(Range _range, Color _col, int _onTime, int _offTime) {
            range = _range;
            col = _col;
            onTime = _onTime;
            offTime = _offTime;
        }

        @Override
        public void update(LEDSubsystem sub) {
            final int cOnTime = Math.max(onTime, 1);
            final int cOffTime = Math.max(offTime, 1);

            Color calc = (timer % (cOnTime + cOffTime) < cOnTime ? col : Color.kBlack);
            for(int i = 0; i < range.length(); i++) {
                sub.setPixel(range, i, calc);
            }
        }
        @Override
        public BlinkAnim[] withRanges(Range... ranges) {
            BlinkAnim[] copies = new BlinkAnim[ranges.length];
            for(int i = 0; i < ranges.length; i++) {
                copies[i] = new BlinkAnim(ranges[i], col, onTime, offTime);
            }

            return copies;
        }
    }
    public static class BreatheAnim extends Animation {

        public static final int DEF_ONTIME = 50;
        public static final int DEF_OFFTIME = 50;

        public Color col;
        public int onTime, offTime;

        public BreatheAnim(Color _col) {
            this(null, _col);
        }
        /**
         * *Note* On and off time is measured in frames. With a update rate of 50 Hz, 50
         * frames is 1
         * second, 25 frames is 0.5 seconds, etc.
         */
        public BreatheAnim(Color _col, int _onTime, int _offTime) {
            this(null, _col, _onTime, _offTime);
        }
        private BreatheAnim(Range _range, Color _col) {
            this(_range, _col, DEF_ONTIME, DEF_OFFTIME);
        }
        private BreatheAnim(Range _range, Color _col, int _onTime, int _offTime) {
            range = _range;
            col = _col;
            onTime = _onTime;
            offTime = _offTime;
        }

        @Override
        public void update(LEDSubsystem sub) {
            final int cOnTime = Math.max(onTime, 1);
            final int cOffTime = Math.max(offTime, 1);

            long t = timer % (cOnTime + cOffTime);
            double val = (t >= cOnTime ? 0 : Math.sin((Math.PI * t) / cOnTime));
            Color calc = new Color(col.red * val, col.green * val, col.blue * val);
            for(int i = 0; i < range.length(); i++) {
                sub.setPixel(range, i, calc);
            }
        }
        @Override
        public BreatheAnim[] withRanges(Range... ranges) {
            BreatheAnim[] copies = new BreatheAnim[ranges.length];
            for(int i = 0; i < ranges.length; i++) {
                copies[i] = new BreatheAnim(ranges[i], col, onTime, offTime);
            }

            return copies;
        }
    }
    public static class ScanAnim extends Animation {

        public static final double DEF_SPEED = 0.6;
        public static final int DEF_DIST = 2;

        public Color col;
        public double speed;
        public int dist;

        public ScanAnim(Color _col) {
            this(null, _col);
        }
        public ScanAnim(Color _col, double _speed, int _dist) {
            this(null, _col, _speed, _dist);
        }
        private ScanAnim(Range _range, Color _col) {
            this(_range, _col, DEF_SPEED, DEF_DIST);
        }
        private ScanAnim(Range _range, Color _col, double _speed, int _dist) {
            range = _range;
            col = _col;
            speed = _speed;
            dist = _dist;
        }

        @Override
        public void update(LEDSubsystem sub) {
            final double cSpeed = Math.max(speed, 0);
            final int cDist = Math.max(dist, 1);

            double t = timer % (2 * (range.length() - 1) / cSpeed);
            int x = (int) Math.round(0.5 * (range.length() - 1) * (Math.cos((Math.PI * t * cSpeed) / (range.length() - 1) + Math.PI) + 1));

            for(int i = 0; i < range.length(); i++) {
                double mult = Math.max(1 - Math.abs(i - x) / (cDist + 1), 0);
                sub.setPixel(range, i, new Color((int) (col.red * mult), (int) (col.green * mult), (int) (col.blue * mult)));
            }
        }
        @Override
        public ScanAnim[] withRanges(Range... ranges) {
            ScanAnim[] copies = new ScanAnim[ranges.length];
            for(int i = 0; i < ranges.length; i++) {
                copies[i] = new ScanAnim(ranges[i], col, speed, dist);
            }

            return copies;
        }
    }

    public static class Range {
        
        public static enum InheritMode {
            COPY,
            MIRROR
        }
        //use if red and green are swapped
        public static enum OutputFormat {
            GRB,
            RGB
        }
        
        private final int start, end;
        private List<Range> children;
        private double brightness;
        private InheritMode inheritMode;
        private OutputFormat outputFormat;
        
        public Range(int _start, int _end) {
            this(_start, _end, InheritMode.COPY, OutputFormat.GRB);
        }
        public Range(int _start, int _end, InheritMode _inheritMode) {
            this(_start, _end, _inheritMode, OutputFormat.GRB);
        }
        public Range(int _start, int _end, OutputFormat _outputFormat) {
            this(_start, _end, InheritMode.COPY, _outputFormat);
        }
        public Range(int _start, int _end, InheritMode _inheritMode, OutputFormat _outputFormat) {
            start = _start;
            end = _end;
            inheritMode = _inheritMode;
            outputFormat = _outputFormat;
            
            children = new ArrayList<>();
            brightness = 1;
        }
        
        public Range(int _start, Range parent) {
            this(_start, parent, InheritMode.COPY, OutputFormat.GRB);
        }
        public Range(int _start, Range parent, InheritMode _inheritMode) {
            this(_start, parent, _inheritMode, OutputFormat.GRB);
        }
        public Range(int _start, Range parent, OutputFormat _outputFormat) {
            this(_start, parent, InheritMode.COPY, _outputFormat);
        }
        public Range(int _start, Range parent, InheritMode _inheritMode, OutputFormat _outputFormat) {
            this(_start, _start + parent.end() - parent.start(), _inheritMode, _outputFormat);

            brightness = parent.brightness();
            parent.withChildren(this);
        }

        protected Range withChildren(Range... ranges) {
            children.addAll(List.of(ranges));
            
            //remove duplicates
            children = new ArrayList<>(new LinkedHashSet<>(children));
            
            return this;
        }
        public Range withBrightness(double _brightness) {
            brightness = Math.min(Math.max(_brightness, 0), 1);
            return this;
        }
        public Range withInheritMode(InheritMode _inheritMode) {
            inheritMode = _inheritMode;
            return this;
        }
        public Range withOutputFormat(OutputFormat _outputFormat) {
            outputFormat = _outputFormat;
            return this;
        }
        
        public int start() {return start;}
        public int end() {return end;}
        public List<Range> children() {return children;}
        public InheritMode inheritMode() {return inheritMode;}
        public double brightness() {return brightness;}
        public int length() {return end - start + 1;}
        public OutputFormat outputFormat() {return outputFormat;}
    }

    public enum LEDProtocol {
        WS2812,     //https://cdn-shop.adafruit.com/datasheets/WS2812.pdf
        WS2812B,    //https://cdn-shop.adafruit.com/datasheets/WS2812B.pdf
        WS2815      //https://www.led-stuebchen.de/download/WS2815.pdf
    }
    
    private final AddressableLED leds;
    private final AddressableLEDBuffer buff;
    private final HashMap<Range, Animation> anims;
    private boolean needsUpdate;

    public LEDSubsystem(LEDProtocol protocol) {
        leds = new AddressableLED(LEDConsts.portID);
        buff = new AddressableLEDBuffer(LEDConsts.ledCount);

        switch(protocol) {
            //default reset time is large enough for all protocols, don't need to change
            
            case WS2812:
                leds.setBitTiming(350, 800, 700, 600);
                break;
            case WS2812B:
                //default behaviour, do nothing
                break;
            case WS2815:
                leds.setBitTiming(300, 1090, 1090, 320);
                break;
        }
        leds.setLength(LEDConsts.ledCount);

        leds.setData(buff);
        leds.start();

        needsUpdate = true;
        anims = new HashMap<>();
    }

    @Override
    public void periodic() {
        updateAnims();

        if(needsUpdate) {
            leds.setData(buff);
            needsUpdate = false;
        }
    }
    @Override
    public void simulationPeriodic() {}

    public void setPixel(Range r, int index, Color col) {
        setPixel(r, index, col, false);
    }
    public void setPixel(Range r, int index, Color col, boolean ignoreBrightness) {
        if(index >= r.length()) return;

        Color modCol;
        if(ignoreBrightness) modCol = col;
        else {
            modCol = new Color((int) (col.red * r.brightness()), (int) (col.green * r.brightness()), (int) (col.blue * r.brightness()));
        }
        int modIndex = (r.inheritMode() == InheritMode.MIRROR ? r.end() - index : r.start() + index);
        
        Color formattedCol;
        switch(r.outputFormat()) {
            default:
            case GRB:
                formattedCol = modCol;
                break;
            
            case RGB:
                formattedCol = new Color(modCol.green, modCol.red, modCol.blue);
                break;
        }
        
        if(modIndex < buff.getLength()) buff.setLED(modIndex, formattedCol);
        
        for(Range child : r.children()) {
            setPixel(child, modIndex, col, ignoreBrightness);
        }

        requestUpdate();
    }

    public void addAnim(Animation anim) {
        if(anim.range() == null) {
            DriverStation.reportWarning("Cannot add an animation with a null range", true);
            return;
        }

        anims.put(anim.range(), anim);
    }
    public void removeAnim(Range r) {
        if(!anims.containsKey(r) || r == null) return;

        anims.remove(r);
    }
    private void updateAnims() {
        for(Animation a : anims.values()) {
            a.update(this);
            a.incTimer();
        }
    }
    
    private void requestUpdate() {
        needsUpdate = true;
    }
}

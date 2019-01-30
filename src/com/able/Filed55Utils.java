package com.able;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Filed55Utils {

    public static List<TagLengthValue> saxUnionField55_2List(String hexfiled55) {

        if (null == hexfiled55) {
            throw new IllegalArgumentException("55域的值不能为空!");
        }

        return builderTLV(hexfiled55);
    }

    private static List<TagLengthValue> builderTLV(String hexString) {
        List<TagLengthValue> tlvs = new ArrayList<TagLengthValue>();

        int position = 0;
        while (position != hexString.length()) {
            String _hexTag = getUnionTag(hexString, position);
            position += _hexTag.length();

            LPositon l_position = getUnionLAndPosition(hexString, position);
            int _vl = l_position.get_vL();

            position = l_position.get_position();

            String _value = hexString.substring(position, position + _vl * 2);

            position = position + _value.length();

            tlvs.add(new TagLengthValue(_hexTag, _vl, _value));
        }
        return tlvs;
    }

    public static Map<String, TagLengthValue> saxUnionField55_2Map(String hexfiled55) {

        if (null == hexfiled55) {
            throw new IllegalArgumentException("55域的值不能为空!");
        }

        return builderKeyAndTLV(hexfiled55);
    }

    public static Map<String, TagLengthValue> builderKeyAndTLV(String hexString) {

        Map<String, TagLengthValue> tlvs = new HashMap<String, TagLengthValue>();

        int position = 0;
        while (position != hexString.length()) {
            String _hexTag = getUnionTag(hexString, position);
            position += _hexTag.length();
            LPositon l_position = getUnionLAndPosition(hexString, position);
            int _vl = l_position.get_vL();
            position = l_position.get_position();
            String _value = hexString.substring(position, position + _vl * 2);
            position = position + _value.length();
            tlvs.put(_hexTag, new TagLengthValue(_hexTag, _vl, _value));
        }
        return tlvs;
    }

    /**
     * 返回最后的Value的长度
     *
     * @param hexString
     * @param position
     * @return
     */
    private static LPositon getUnionLAndPosition(String hexString, int position) {

        String firstByteString = hexString.substring(position, position + 2);
        int i = Integer.parseInt(firstByteString, 16);
        String hexLength = "";

        if (((i >>> 7) & 1) == 0) {
            hexLength = hexString.substring(position, position + 2);
            position = position + 2;

        } else {
            // 当最左侧的bit位为1的时候，取得后7bit的值，
            int _L_Len = i & 127;
            position = position + 2;
            hexLength = hexString.substring(position, position + _L_Len * 2);
            // position表示第一个字节，后面的表示有多少个字节来表示后面的Value值
            position = position + _L_Len * 2;

        }
        return new LPositon(Integer.parseInt(hexLength, 16), position);

    }

    private static String getUnionTag(String hexString, int position) {
        String firstByte = hexString.substring(position, position + 2);
        int i = Integer.parseInt(firstByte, 16);
        if ((i & 0x0f) == 0x0f) {
            return hexString.substring(position, position + 4);

        } else {
            return hexString.substring(position, position + 2);
        }

    }

    static class LPositon {
        private int _vL;
        private int _position;

        public LPositon(int _vL, int position) {
            this._vL = _vL;
            this._position = position;
        }

        public int get_vL() {
            return _vL;
        }

        public void set_vL(int _vL) {
            this._vL = _vL;
        }

        public int get_position() {
            return _position;
        }

        public void set_position(int _position) {
            this._position = _position;
        }

    }

    public static void main(String[] args) {
        List<TagLengthValue> list = Filed55Utils.saxUnionField55_2List("9F2608E22CD3AFA54C91B49F2701809F1013072F0103A0B000010A0100000000009D8C29F69F370481B9A27C9F36020030950508080460009A031902239C01319F02060000000000005F2A02015682027C009F1A0208409F03060000000000009F33036040E09F34030203009F3501149F090200309F1E085465726D696E616C");
        for (TagLengthValue tlv : list) {
            System.out.println(tlv);
        }
    }

}
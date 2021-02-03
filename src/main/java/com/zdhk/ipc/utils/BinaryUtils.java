package com.zdhk.ipc.utils;

public class BinaryUtils {

    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


    /**
     * int类型数据src转成byte数组result
     * @param result
     * @param src
     * @param index
     */
    public static void intToByteArray(byte[] result, int src, int index){
        result[index + 0] = (byte) (src >> 24); //最高位包存到0下标
        result[index + 1] = (byte) (src >> 16); //最低位保存到1下标
        result[index + 2] = (byte) (src >> 8);
        result[index + 3] = (byte) (src >> 0);
    }

    //byte数组转成int，根据index（下标）每次转四个byte数据
    public static int ByteArrayToInt(byte[] traget, int index){
        return  ((((traget[index + 0] &0xff) << 24) | (traget[index + 1] &0xff) << 16 | (traget[index + 2] &0xff) << 8 | (traget[index + 3] & 0xff)));
    }

    public static byte[] reserve( byte[] arr ){
        byte[] arr1 = new byte[arr.length];
        for( int x=0;x<arr.length;x++ ){
            arr1[x] = arr[arr.length-x-1];
        }
        return arr1 ;
    }

    public  static void byteToByteArray(byte[] bys, byte src, int index){
        bys[index] = src;
    }

    public  static void shortToByteArray(byte[] bys, short src, int index){
        bys[index] = (byte)(src>>8);
        bys[index+1] = (byte)(src>>0);
    }

    public  static byte[] getShortToByteArrayReverse(short src){
        byte[] bys = new byte[2];
        bys[0] = (byte)(src>>0);
        bys[1] = (byte)(src>>8);
        return bys;
    }

    public  static void shortToByteArrayReverse(byte[] bys, short src, int index){
        bys[index] = (byte)(src>>0);
        bys[index+1] = (byte)(src>>8);
    }


    public static short ByteArrayToShort(byte[] target,int index){
        return (short)(((target[index+0] & 0xff)<<8 | (target[index+1]&0xff)));
    }

    public static short ByteArrayToShortReserve(byte[] target,int index){
        return (short)(((target[index+1] & 0xff)<<8 | (target[index+0]&0xff)));
    }


    public  static void intToByteArrayReverse(byte[] bys, int src, int index){
        bys[index] = (byte)(src>>0);
        bys[index+1] = (byte)(src>>8);
        bys[index+2] = (byte)(src>>16);
        bys[index+3] = (byte)(src>>24);
    }

    public  static byte[] getIntToByteArrayReverse(int src){
        byte[] bys = new byte[4];
        bys[0] = (byte)(src>>0);
        bys[1] = (byte)(src>>8);
        bys[2] = (byte)(src>>16);
        bys[3] = (byte)(src>>24);
        return bys;
    }


    public static int ByteArrayToIntReserve(byte[] target,int index){
        return (int)(((target[index+3] & 0xffff)<<24|(target[index+2] & 0xffff)<<16|(target[index+1] & 0xffff)<<8 | (target[index+0]&0xffff)));
    }



    /**
     * 截取byte数组从index开始截取length长度，返回截取的数组
     * @param src 原始数据
     * @param index 原始数据哪个开始
     * @param len  截取长度
     * @return
     */
    public static byte[] subArrayIndexLength(byte[] src,int index,int len){
        byte[] result = new byte[len];
        System.arraycopy(src, index, result, 0, len);
        return result;
    }

    /**
     * 4字节
     * @param a
     * @return
     */
    public static String getIpBybyteArr(byte[] a) {
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<a.length;i++){
            if(i==a.length-1){
                sb.append((int)(a[i]&0xff));
            }else{
                sb.append((int)(a[i]&0xff));
                sb.append(".");
            }
        }
        return sb.toString();
    }

    /**
     * byte转int之前需要&0xff用于去除负号
     * @param b
     * @return
     */
    public static int byteToInt(byte b){
        return (int)(b&0xff);
    }

    /**
     * byte[]转16进制字符串
     * @param bytes
     * @return
     */
    public static String bytesToHex(byte[] bytes) {
        // 一个byte为8位，可用两个十六进制位标识
        char[] buf = new char[bytes.length * 2];
        int a = 0;
        int index = 0;
        for(byte b : bytes) { // 使用除与取余进行转换
            if(b < 0) {
                a = 256 + b;
            } else {
                a = b;
            }
            buf[index++] = HEX_CHAR[a / 16];
            buf[index++] = HEX_CHAR[a % 16];
        }
        return new String(buf);
    }

    /**
     * 将16进制字符串转换为byte[]
     * @explain 16进制字符串不区分大小写，返回的数组相同
     * @param hexString
     *            16进制字符串
     * @return byte[]
     */
    public static byte[] fromHexString(String hexString) {
        if (null == hexString || "".equals(hexString.trim())) {
            return new byte[0];
        }

        byte[] bytes = new byte[hexString.length() / 2];
        // 16进制字符串
        String hex;
        for (int i = 0; i < hexString.length() / 2; i++) {
            // 每次截取2位
            hex = hexString.substring(i * 2, i * 2 + 2);
            // 16进制-->十进制
            bytes[i] = (byte) Integer.parseInt(hex, 16);
        }

        return bytes;
    }


    private void getLineByChannelId(Integer channelId){
        int ge = (channelId/1)%10;
        int shi = (channelId/10)%10;
        int bai = (channelId/100)%10;
        int qian = (channelId/1000)%10;
    }




    public static void main(String[] args) {

    }

}

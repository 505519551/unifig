public class Turns {
    //测试main方法
    public static void main(String[] args) {
        String [] a={"a"};
        String [] b={"b"};
        String [] c={"c"};
        String [] d={"d"};
        String[] add = new Turns().turns(a, b,c,d);
        for (String string : add) {
            System.out.println(string);
        }

    }
    /**
     * 两两遍历
     * @param array1
     * @param array2
     * @return
     */
    public static String[] doubleTurns(String [] array1,String[] array2){
        String [] target=new String[array1.length*array2.length];
        for (int i = 0,a1=0,a2=0; i <array1.length*array2.length; i++) {
            target[i]=array1[a1]+","+array2[a2];
            a2++;
            if(a2==array2.length){
                a2=0;
                a1++;
            }
        }
        return target;
    }
    /**
     * 遍历组合
     * @param arrays
     * @return
     */
    public static String[] turns(String[] ...arrays){
        if(arrays.length==1){
            return arrays[0];
        }
        if(arrays.length==0){
            return null;
        }
        //获得总结果数
        int count=0;
        for (int i = 0; i < arrays.length; i++) {
            count*=arrays[i].length;
        }
        String target[]=new String[count];
        //两两遍历
        for (int i = 0; i < arrays.length; i++) {
            if(i==0){
                target=doubleTurns(arrays[0],arrays[1]);
                i++;
            }else{
                target=doubleTurns(target,arrays[i]);
            }
        }
        return target;
    }
}
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class app {
    public static void main(String[] args) {
        String s = "EURNOK:10.10;EURSEK:10.20;EURGBP:0.86|GBPJPY";

        determineExchangeRate(readPairs(s));
    }

    static class Pair{
        public String getBase() {
            return base;
        }

        public void setBase(String base) {
            this.base = base;
        }

        public String getQuote() {
            return quote;
        }

        public void setQuote(String quote) {
            this.quote = quote;
        }

        public float getRate() {
            return rate;
        }

        public void setRate(float rate) {
            this.rate = rate;
        }

        String base;
        String quote;
        float rate;


        public Pair(String base, String quote, float rate) {
            this.base = base;
            this.quote = quote;
            this.rate = rate;
        }
    }

    public static List<Pair> readPairs(String str){
        String[] sub = str.split(";");
        List<Pair> Pairs = new ArrayList();
        for ( int rate=0 ; rate<=(sub.length -1); rate++){
           String currency = sub[rate];
           if (rate == sub.length -1)
           {
               String[] spl = currency.split("\\|"); // last pair
               String[] val = spl[0].split(":");
               String[] d = val[0].split("(?<=\\G.{3})"); // split cur by 3
               Pairs.add(new Pair(d[0], d[1], Float.parseFloat(val[1])));
               String[] ch = spl[1].split("(?<=\\G.{3})");
               Pairs.add(new Pair(ch[0], ch[1], 0));
           }
           else
               {
                String[] val  = currency.split(":");
                String[] d = val[0].split("(?<=\\G.{3})");
                Pairs.add(new Pair(d[0], d[1], Float.parseFloat(val[1])));
               }

           }

        return Pairs;
    }

    public static void determineExchangeRate(List<Pair> pairs){
        Pair conv = pairs.remove(pairs.size()-1);
        // check for same name
        if (conv.getBase().equals(conv.getQuote())){
            System.out.println(conv.getBase()+conv.getBase()+":1.00");
            return;
        }
        for ( Pair pair :pairs)
        {
            if ((pair.getBase().equals(conv.getBase())
               & (pair.getQuote().equals(conv.getQuote())))) {
                System.out.println(pair.getBase()+pair.getQuote()+
                        ':'+pair.getRate());
                return;
            }
            else if (pair.getBase().equals(conv.getQuote())
                && pair.getQuote().equals(conv.getBase()))
            {
                System.out.println(pair.getQuote()+pair.getBase()+
                        ':'+(Math.round(1/ pair.getRate()* 100.0) / 100.0));
                return;
            }
            else {

            }
        }
        System.out.println("Unable to determine rate for "+conv.getBase()+
                conv.getQuote());
    }
}

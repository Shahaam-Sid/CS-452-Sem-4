package com.shahaam.equipend.utils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AppConstants {
    public static final List<String> COUNTRIES = List.of(
        "afghanistan",
        "albania",
        "algeria",
        "andorra",
        "angola",
        "argentina",
        "armenia",
        "australia",
        "austria",
        "azerbaijan",
        "bahamas",
        "bahrain",
        "bangladesh",
        "barbados",
        "belarus",
        "belgium",
        "belize",
        "benin",
        "bhutan",
        "bolivia",
        "bosnia and herzegovina",
        "botswana",
        "brazil",
        "brunei",
        "bulgaria",
        "burkina faso",
        "burundi",
        "cambodia",
        "cameroon",
        "canada",
        "cape verde",
        "central african republic",
        "chad",
        "chile",
        "china",
        "colombia",
        "comoros",
        "congo (brazzaville)",
        "congo (kinshasa)",
        "costa rica",
        "croatia",
        "cuba",
        "cyprus",
        "czech republic",
        "denmark",
        "djibouti",
        "dominica",
        "dominican republic",
        "ecuador",
        "egypt",
        "el salvador",
        "equatorial guinea",
        "eritrea",
        "estonia",
        "eswatini",
        "ethiopia",
        "fiji",
        "finland",
        "france",
        "gabon",
        "gambia",
        "georgia",
        "germany",
        "ghana",
        "greece",
        "grenada",
        "guatemala",
        "guinea",
        "guinea-bissau",
        "guyana",
        "haiti",
        "honduras",
        "hungary",
        "iceland",
        "india",
        "indonesia",
        "iran",
        "iraq",
        "ireland",
        "israel",
        "italy",
        "ivory coast",
        "jamaica",
        "japan",
        "jordan",
        "kazakhstan",
        "kenya",
        "kiribati",
        "kuwait",
        "kyrgyzstan",
        "laos",
        "latvia",
        "lebanon",
        "lesotho",
        "liberia",
        "libya",
        "liechtenstein",
        "lithuania",
        "luxembourg",
        "madagascar",
        "malawi",
        "malaysia",
        "maldives",
        "mali",
        "malta",
        "marshall islands",
        "mauritania",
        "mauritius",
        "mexico",
        "micronesia",
        "moldova",
        "monaco",
        "mongolia",
        "montenegro",
        "morocco",
        "mozambique",
        "myanmar",
        "namibia",
        "nauru",
        "nepal",
        "netherlands",
        "new zealand",
        "nicaragua",
        "niger",
        "nigeria",
        "north korea",
        "north macedonia",
        "norway",
        "oman",
        "pakistan",
        "palau",
        "palestine",
        "panama",
        "papua new guinea",
        "paraguay",
        "peru",
        "philippines",
        "poland",
        "portugal",
        "qatar",
        "romania",
        "russia",
        "rwanda",
        "saint kitts and nevis",
        "saint lucia",
        "saint vincent and the grenadines",
        "samoa",
        "san marino",
        "sao tome and principe",
        "saudi arabia",
        "senegal",
        "serbia",
        "seychelles",
        "sierra leone",
        "singapore",
        "slovakia",
        "slovenia",
        "solomon islands",
        "somalia",
        "south africa",
        "south korea",
        "south sudan",
        "spain",
        "sri lanka",
        "sudan",
        "suriname",
        "sweden",
        "switzerland",
        "syria",
        "taiwan",
        "tajikistan",
        "tanzania",
        "thailand",
        "timor-leste",
        "togo",
        "tonga",
        "trinidad and tobago",
        "tunisia",
        "turkey",
        "turkmenistan",
        "tuvalu",
        "uganda",
        "ukraine",
        "united arab emirates",
        "united kingdom",
        "united states",
        "uruguay",
        "uzbekistan",
        "vanuatu",
        "vatican city",
        "venezuela",
        "vietnam",
        "yemen",
        "zambia",
        "zimbabwe"
    );

     public static final Map<String, String> CURRENCIES;

    static {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("aed", "uae dirham");
        m.put("afn", "afghan afghani");
        m.put("all", "albanian lek");
        m.put("amd", "armenian dram");
        m.put("ang", "netherlands antillean guilder");
        m.put("aoa", "angolan kwanza");
        m.put("ars", "argentine peso");
        m.put("aud", "australian dollar");
        m.put("awg", "aruban florin");
        m.put("azn", "azerbaijani manat");
        m.put("bam", "bosnia-herzegovina convertible mark");
        m.put("bbd", "barbadian dollar");
        m.put("bdt", "bangladeshi taka");
        m.put("bhd", "bahraini dinar");
        m.put("bif", "burundian franc");
        m.put("bmd", "bermudian dollar");
        m.put("bnd", "brunei dollar");
        m.put("bob", "bolivian boliviano");
        m.put("brl", "brazilian real");
        m.put("bsd", "bahamian dollar");
        m.put("btn", "bhutanese ngultrum");
        m.put("bwp", "botswana pula");
        m.put("byn", "belarusian ruble");
        m.put("bzd", "belize dollar");
        m.put("cad", "canadian dollar");
        m.put("cdf", "congolese franc");
        m.put("chf", "swiss franc");
        m.put("clp", "chilean peso");
        m.put("cny", "chinese yuan");
        m.put("cop", "colombian peso");
        m.put("crc", "costa rican colón");
        m.put("cup", "cuban peso");
        m.put("cve", "cape verdean escudo");
        m.put("czk", "czech koruna");
        m.put("djf", "djiboutian franc");
        m.put("dkk", "danish krone");
        m.put("dop", "dominican peso");
        m.put("dzd", "algerian dinar");
        m.put("egp", "egyptian pound");
        m.put("ern", "eritrean nakfa");
        m.put("etb", "ethiopian birr");
        m.put("eur", "euro");
        m.put("fjd", "fijian dollar");
        m.put("gbp", "british pound sterling");
        m.put("gel", "georgian lari");
        m.put("ghs", "ghanaian cedi");
        m.put("gip", "gibraltar pound");
        m.put("gmd", "gambian dalasi");
        m.put("gnf", "guinean franc");
        m.put("gtq", "guatemalan quetzal");
        m.put("gyd", "guyanese dollar");
        m.put("hkd", "hong kong dollar");
        m.put("hnl", "honduran lempira");
        m.put("htg", "haitian gourde");
        m.put("huf", "hungarian forint");
        m.put("idr", "indonesian rupiah");
        m.put("ils", "israeli new shekel");
        m.put("inr", "indian rupee");
        m.put("iqd", "iraqi dinar");
        m.put("irr", "iranian rial");
        m.put("isk", "icelandic króna");
        m.put("jmd", "jamaican dollar");
        m.put("jod", "jordanian dinar");
        m.put("jpy", "japanese yen");
        m.put("kes", "kenyan shilling");
        m.put("kgs", "kyrgyzstani som");
        m.put("khr", "cambodian riel");
        m.put("kmf", "comorian franc");
        m.put("kpw", "north korean won");
        m.put("krw", "south korean won");
        m.put("kwd", "kuwaiti dinar");
        m.put("kyd", "cayman islands dollar");
        m.put("kzt", "kazakhstani tenge");
        m.put("lak", "lao kip");
        m.put("lbp", "lebanese pound");
        m.put("lkr", "sri lankan rupee");
        m.put("lrd", "liberian dollar");
        m.put("lsl", "lesotho loti");
        m.put("lyd", "libyan dinar");
        m.put("mad", "moroccan dirham");
        m.put("mdl", "moldovan leu");
        m.put("mga", "malagasy ariary");
        m.put("mkd", "north macedonian denar");
        m.put("mmk", "myanmar kyat");
        m.put("mnt", "mongolian tögrög");
        m.put("mop", "macanese pataca");
        m.put("mru", "mauritanian ouguiya");
        m.put("mur", "mauritian rupee");
        m.put("mvr", "maldivian rufiyaa");
        m.put("mwk", "malawian kwacha");
        m.put("mxn", "mexican peso");
        m.put("myr", "malaysian ringgit");
        m.put("mzn", "mozambican metical");
        m.put("nad", "namibian dollar");
        m.put("ngn", "nigerian naira");
        m.put("nio", "nicaraguan córdoba");
        m.put("nok", "norwegian krone");
        m.put("npr", "nepalese rupee");
        m.put("nzd", "new zealand dollar");
        m.put("omr", "omani rial");
        m.put("pab", "panamanian balboa");
        m.put("pen", "peruvian sol");
        m.put("pgk", "papua new guinean kina");
        m.put("php", "philippine peso");
        m.put("pkr", "pakistani rupee");
        m.put("pln", "polish złoty");
        m.put("pyg", "paraguayan guaraní");
        m.put("qar", "qatari riyal");
        m.put("ron", "romanian leu");
        m.put("rsd", "serbian dinar");
        m.put("rub", "russian ruble");
        m.put("rwf", "rwandan franc");
        m.put("sar", "saudi riyal");
        m.put("sbd", "solomon islands dollar");
        m.put("scr", "seychellois rupee");
        m.put("sdg", "sudanese pound");
        m.put("sek", "swedish krona");
        m.put("sgd", "singapore dollar");
        m.put("shp", "saint helena pound");
        m.put("sle", "sierra leonean leone");
        m.put("sos", "somali shilling");
        m.put("srd", "surinamese dollar");
        m.put("ssp", "south sudanese pound");
        m.put("stn", "são tomé and príncipe dobra");
        m.put("syp", "syrian pound");
        m.put("szl", "swazi lilangeni");
        m.put("thb", "thai baht");
        m.put("tjs", "tajikistani somoni");
        m.put("tmt", "turkmenistani manat");
        m.put("tnd", "tunisian dinar");
        m.put("top", "tongan paʻanga");
        m.put("try", "turkish lira");
        m.put("ttd", "trinidad and tobago dollar");
        m.put("twd", "new taiwan dollar");
        m.put("tzs", "tanzanian shilling");
        m.put("uah", "ukrainian hryvnia");
        m.put("ugx", "ugandan shilling");
        m.put("usd", "united states dollar");
        m.put("uyu", "uruguayan peso");
        m.put("uzs", "uzbekistani som");
        m.put("ves", "venezuelan bolívar");
        m.put("vnd", "vietnamese đồng");
        m.put("vuv", "vanuatu vatu");
        m.put("wst", "samoan tālā");
        m.put("xaf", "cfa franc beac");
        m.put("xcd", "east caribbean dollar");
        m.put("xof", "cfa franc bceao");
        m.put("xpf", "cfp franc");
        m.put("yer", "yemeni rial");
        m.put("zar", "south african rand");
        m.put("zmw", "zambian kwacha");
        m.put("zwg", "zimbabwe gold");
        CURRENCIES = Collections.unmodifiableMap(m);
    }
}
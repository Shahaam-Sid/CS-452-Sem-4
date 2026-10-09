package com.shahaam.equipend.utils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AppConstants {
    public static final List<String> COUNTRIES = List.of(
        "Afghanistan", "Albania", "Algeria", "Andorra", "Angola",
        "Argentina", "Armenia", "Australia", "Austria", "Azerbaijan",
        "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus",
        "Belgium", "Belize", "Benin", "Bhutan", "Bolivia",
        "Bosnia and Herzegovina", "Botswana", "Brazil", "Brunei", "Bulgaria",
        "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada",
        "Cape Verde", "Central African Republic", "Chad", "Chile", "China",
        "Colombia", "Comoros", "Congo (Brazzaville)", "Congo (Kinshasa)", "Costa Rica",
        "Croatia", "Cuba", "Cyprus", "Czech Republic", "Denmark",
        "Djibouti", "Dominica", "Dominican Republic", "Ecuador", "Egypt",
        "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Eswatini",
        "Ethiopia", "Fiji", "Finland", "France", "Gabon",
        "Gambia", "Georgia", "Germany", "Ghana", "Greece",
        "Grenada", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana",
        "Haiti", "Honduras", "Hungary", "Iceland", "India",
        "Indonesia", "Iran", "Iraq", "Ireland", "Israel",
        "Italy", "Ivory Coast", "Jamaica", "Japan", "Jordan",
        "Kazakhstan", "Kenya", "Kiribati", "Kuwait", "Kyrgyzstan",
        "Laos", "Latvia", "Lebanon", "Lesotho", "Liberia",
        "Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Madagascar",
        "Malawi", "Malaysia", "Maldives", "Mali", "Malta",
        "Marshall Islands", "Mauritania", "Mauritius", "Mexico", "Micronesia",
        "Moldova", "Monaco", "Mongolia", "Montenegro", "Morocco",
        "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal",
        "Netherlands", "New Zealand", "Nicaragua", "Niger", "Nigeria",
        "North Korea", "North Macedonia", "Norway", "Oman", "Pakistan",
        "Palau", "Palestine", "Panama", "Papua New Guinea", "Paraguay",
        "Peru", "Philippines", "Poland", "Portugal", "Qatar",
        "Romania", "Russia", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia",
        "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia",
        "Senegal", "Serbia", "Seychelles", "Sierra Leone", "Singapore",
        "Slovakia", "Slovenia", "Solomon Islands", "Somalia", "South Africa",
        "South Korea", "South Sudan", "Spain", "Sri Lanka", "Sudan",
        "Suriname", "Sweden", "Switzerland", "Syria", "Taiwan",
        "Tajikistan", "Tanzania", "Thailand", "Timor-Leste", "Togo",
        "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan",
        "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom",
        "United States", "Uruguay", "Uzbekistan", "Vanuatu", "Vatican City",
        "Venezuela", "Vietnam", "Yemen", "Zambia", "Zimbabwe"
    );

     public static final Map<String, String> CURRENCIES;

    static {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("AED", "UAE Dirham");
        m.put("AFN", "Afghan Afghani");
        m.put("ALL", "Albanian Lek");
        m.put("AMD", "Armenian Dram");
        m.put("ANG", "Netherlands Antillean Guilder");
        m.put("AOA", "Angolan Kwanza");
        m.put("ARS", "Argentine Peso");
        m.put("AUD", "Australian Dollar");
        m.put("AWG", "Aruban Florin");
        m.put("AZN", "Azerbaijani Manat");
        m.put("BAM", "Bosnia-Herzegovina Convertible Mark");
        m.put("BBD", "Barbadian Dollar");
        m.put("BDT", "Bangladeshi Taka");
        m.put("BHD", "Bahraini Dinar");
        m.put("BIF", "Burundian Franc");
        m.put("BMD", "Bermudian Dollar");
        m.put("BND", "Brunei Dollar");
        m.put("BOB", "Bolivian Boliviano");
        m.put("BRL", "Brazilian Real");
        m.put("BSD", "Bahamian Dollar");
        m.put("BTN", "Bhutanese Ngultrum");
        m.put("BWP", "Botswana Pula");
        m.put("BYN", "Belarusian Ruble");
        m.put("BZD", "Belize Dollar");
        m.put("CAD", "Canadian Dollar");
        m.put("CDF", "Congolese Franc");
        m.put("CHF", "Swiss Franc");
        m.put("CLP", "Chilean Peso");
        m.put("CNY", "Chinese Yuan");
        m.put("COP", "Colombian Peso");
        m.put("CRC", "Costa Rican Colón");
        m.put("CUP", "Cuban Peso");
        m.put("CVE", "Cape Verdean Escudo");
        m.put("CZK", "Czech Koruna");
        m.put("DJF", "Djiboutian Franc");
        m.put("DKK", "Danish Krone");
        m.put("DOP", "Dominican Peso");
        m.put("DZD", "Algerian Dinar");
        m.put("EGP", "Egyptian Pound");
        m.put("ERN", "Eritrean Nakfa");
        m.put("ETB", "Ethiopian Birr");
        m.put("EUR", "Euro");
        m.put("FJD", "Fijian Dollar");
        m.put("GBP", "British Pound Sterling");
        m.put("GEL", "Georgian Lari");
        m.put("GHS", "Ghanaian Cedi");
        m.put("GIP", "Gibraltar Pound");
        m.put("GMD", "Gambian Dalasi");
        m.put("GNF", "Guinean Franc");
        m.put("GTQ", "Guatemalan Quetzal");
        m.put("GYD", "Guyanese Dollar");
        m.put("HKD", "Hong Kong Dollar");
        m.put("HNL", "Honduran Lempira");
        m.put("HTG", "Haitian Gourde");
        m.put("HUF", "Hungarian Forint");
        m.put("IDR", "Indonesian Rupiah");
        m.put("ILS", "Israeli New Shekel");
        m.put("INR", "Indian Rupee");
        m.put("IQD", "Iraqi Dinar");
        m.put("IRR", "Iranian Rial");
        m.put("ISK", "Icelandic Króna");
        m.put("JMD", "Jamaican Dollar");
        m.put("JOD", "Jordanian Dinar");
        m.put("JPY", "Japanese Yen");
        m.put("KES", "Kenyan Shilling");
        m.put("KGS", "Kyrgyzstani Som");
        m.put("KHR", "Cambodian Riel");
        m.put("KMF", "Comorian Franc");
        m.put("KPW", "North Korean Won");
        m.put("KRW", "South Korean Won");
        m.put("KWD", "Kuwaiti Dinar");
        m.put("KYD", "Cayman Islands Dollar");
        m.put("KZT", "Kazakhstani Tenge");
        m.put("LAK", "Lao Kip");
        m.put("LBP", "Lebanese Pound");
        m.put("LKR", "Sri Lankan Rupee");
        m.put("LRD", "Liberian Dollar");
        m.put("LSL", "Lesotho Loti");
        m.put("LYD", "Libyan Dinar");
        m.put("MAD", "Moroccan Dirham");
        m.put("MDL", "Moldovan Leu");
        m.put("MGA", "Malagasy Ariary");
        m.put("MKD", "North Macedonian Denar");
        m.put("MMK", "Myanmar Kyat");
        m.put("MNT", "Mongolian Tögrög");
        m.put("MOP", "Macanese Pataca");
        m.put("MRU", "Mauritanian Ouguiya");
        m.put("MUR", "Mauritian Rupee");
        m.put("MVR", "Maldivian Rufiyaa");
        m.put("MWK", "Malawian Kwacha");
        m.put("MXN", "Mexican Peso");
        m.put("MYR", "Malaysian Ringgit");
        m.put("MZN", "Mozambican Metical");
        m.put("NAD", "Namibian Dollar");
        m.put("NGN", "Nigerian Naira");
        m.put("NIO", "Nicaraguan Córdoba");
        m.put("NOK", "Norwegian Krone");
        m.put("NPR", "Nepalese Rupee");
        m.put("NZD", "New Zealand Dollar");
        m.put("OMR", "Omani Rial");
        m.put("PAB", "Panamanian Balboa");
        m.put("PEN", "Peruvian Sol");
        m.put("PGK", "Papua New Guinean Kina");
        m.put("PHP", "Philippine Peso");
        m.put("PKR", "Pakistani Rupee");
        m.put("PLN", "Polish Złoty");
        m.put("PYG", "Paraguayan Guaraní");
        m.put("QAR", "Qatari Riyal");
        m.put("RON", "Romanian Leu");
        m.put("RSD", "Serbian Dinar");
        m.put("RUB", "Russian Ruble");
        m.put("RWF", "Rwandan Franc");
        m.put("SAR", "Saudi Riyal");
        m.put("SBD", "Solomon Islands Dollar");
        m.put("SCR", "Seychellois Rupee");
        m.put("SDG", "Sudanese Pound");
        m.put("SEK", "Swedish Krona");
        m.put("SGD", "Singapore Dollar");
        m.put("SHP", "Saint Helena Pound");
        m.put("SLE", "Sierra Leonean Leone");
        m.put("SOS", "Somali Shilling");
        m.put("SRD", "Surinamese Dollar");
        m.put("SSP", "South Sudanese Pound");
        m.put("STN", "São Tomé and Príncipe Dobra");
        m.put("SYP", "Syrian Pound");
        m.put("SZL", "Swazi Lilangeni");
        m.put("THB", "Thai Baht");
        m.put("TJS", "Tajikistani Somoni");
        m.put("TMT", "Turkmenistani Manat");
        m.put("TND", "Tunisian Dinar");
        m.put("TOP", "Tongan Paʻanga");
        m.put("TRY", "Turkish Lira");
        m.put("TTD", "Trinidad and Tobago Dollar");
        m.put("TWD", "New Taiwan Dollar");
        m.put("TZS", "Tanzanian Shilling");
        m.put("UAH", "Ukrainian Hryvnia");
        m.put("UGX", "Ugandan Shilling");
        m.put("USD", "United States Dollar");
        m.put("UYU", "Uruguayan Peso");
        m.put("UZS", "Uzbekistani Som");
        m.put("VES", "Venezuelan Bolívar");
        m.put("VND", "Vietnamese Đồng");
        m.put("VUV", "Vanuatu Vatu");
        m.put("WST", "Samoan Tālā");
        m.put("XAF", "CFA Franc BEAC");
        m.put("XCD", "East Caribbean Dollar");
        m.put("XOF", "CFA Franc BCEAO");
        m.put("XPF", "CFP Franc");
        m.put("YER", "Yemeni Rial");
        m.put("ZAR", "South African Rand");
        m.put("ZMW", "Zambian Kwacha");
        m.put("ZWG", "Zimbabwe Gold");
        CURRENCIES = Collections.unmodifiableMap(m);
    }
}
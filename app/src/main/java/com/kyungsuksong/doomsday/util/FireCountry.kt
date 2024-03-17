package com.kyungsuksong.doomsday.util

import com.google.android.gms.maps.model.LatLng

object FireCountry {
    val fireCountryMap: Map<String, String> = mapOf(
        "Aruba" to "ABW",
        "Afghanistan" to "AFG",
        "Angola" to "AGO",
        "Anguilla" to "AIA",
        "Aland Islands" to "ALA",
        "Albania" to "ALB",
        "Andorra" to "AND",
        "United Arab Emirates" to "ARE",
        "Argentina" to "ARG",
        "Armenia" to "ARM",
        "American Samoa" to "ASM",
        "Antarctica" to "ATA",
        "French Southern and Antarctic Lands" to "ATF",
        "Antigua and Barbuda" to "ATG",
        "Australia" to "AUS",
        "Austria" to "AUT",
        "Azerbaijan" to "AZE",
        "Burundi" to "BDI",
        "Belgium" to "BEL",
        "Benin" to "BEN",
        "Burkina Faso" to "BFA",
        "Bangladesh" to "BGD",
        "Bulgaria" to "BGR",
        "Bahrain" to "BHR",
        "Bahamas" to "BHS",
        "Bosnia and Herzegovina" to "BIH",
        "Saint-Barthelemy" to "BLM",
        "Belarus" to "BLR",
        "Belize" to "BLZ",
        "Bermuda" to "BMU",
        "Bolivia" to "BOL",
        "Brazil" to "BRA",
        "Barbados" to "BRB",
        "Brunei Darussalam" to "BRN",
        "Bhutan" to "BTN",
        "Botswana" to "BWA",
        "Central African Republic" to "CAF",
        "Canada" to "CAN",
        "Switzerland" to "CHE",
        "Chile" to "CHL",
        "China" to "CHN",
        "Cote d'Ivoire" to "CIV",
        "Cameroon" to "CMR",
        "Democratic Republic of the Congo" to "COD",
        "Republic of Congo" to "COG",
        "Cook Islands" to "COK",
        "Colombia" to "COL",
        "Comoros" to "COM",
        "Cape Verde" to "CPV",
        "Costa Rica" to "CRI",
        "Cuba" to "CUB",
        "Curacao" to "CUW",
        "Cayman Islands" to "CYM",
        "Cyprus" to "CYP",
        "Czech Republic" to "CZE",
        "Germany" to "DEU",
        "Djibouti" to "DJI",
        "Dominica" to "DMA",
        "Denmark" to "DNK",
        "Dominican Republic" to "DOM",
        "Algeria" to "DZA",
        "Ecuador" to "ECU",
        "Egypt" to "EGY",
        "Eritrea" to "ERI",
        "Spain" to "ESP",
        "Estonia" to "EST",
        "Ethiopia" to "ETH",
        "Finland" to "FIN",
        "Fiji" to "FJI",
        "Falkland Islands" to "FLK",
        "France" to "FRA",
        "Faeroe Islands" to "FRO",
        "Federated States of Micronesia" to "FSM",
        "Gabon" to "GAB",
        "United Kingdom" to "GBR",
        "Georgia" to "GEO",
        "Guernsey" to "GGY",
        "Ghana" to "GHA",
        "Gibraltar" to "GIB",
        "Guinea" to "GIN",
        "Guadeloupe" to "GLP",
        "The Gambia" to "GMB",
        "Guinea-Bissau" to "GNB",
        "Equatorial Guinea" to "GNQ",
        "Greece" to "GRC",
        "Grenada" to "GRD",
        "Greenland" to "GRL",
        "Guatemala" to "GTM",
        "French Guiana" to "GUF",
        "Guam" to "GUM",
        "Guyana" to "GUY",
        "Hong Kong" to "HKG",
        "Heard I. and McDonald Islands" to "HMD",
        "Honduras" to "HND",
        "Croatia" to "HRV",
        "Haiti" to "HTI",
        "Hungary" to "HUN",
        "Indonesia" to "IDN",
        "Isle of Man" to "IMN",
        "India" to "IND",
        "British Indian Ocean Territory" to "IOT",
        "Ireland" to "IRL",
        "Iran" to "IRN",
        "Iraq" to "IRQ",
        "Iceland" to "ISL",
        "Israel" to "ISR",
        "Italy" to "ITA",
        "Jamaica" to "JAM",
        "Jersey" to "JEY",
        "Jordan" to "JOR",
        "Japan" to "JPN",
        "Kazakhstan" to "KAZ",
        "Kenya" to "KEN",
        "Kyrgyzstan" to "KGZ",
        "Cambodia" to "KHM",
        "Kiribati" to "KIR",
        "Saint Kitts and Nevis" to "KNA",
        "Republic of Korea" to "KOR",
        "Kosovo" to "KOS",
        "Kuwait" to "KWT",
        "Lao PDR" to "LAO",
        "Lebanon" to "LBN",
        "Liberia" to "LBR",
        "Libya" to "LBY",
        "Saint Lucia" to "LCA",
        "Liechtenstein" to "LIE",
        "Sri Lanka" to "LKA",
        "Lesotho" to "LSO",
        "Lithuania" to "LTU",
        "Luxembourg" to "LUX",
        "Latvia" to "LVA",
        "Macao" to "MAC",
        "Saint-Martin" to "MAF",
        "Morocco" to "MAR",
        "Monaco" to "MCO",
        "Moldova" to "MDA",
        "Madagascar" to "MDG",
        "Maldives" to "MDV",
        "Mexico" to "MEX",
        "Marshall Islands" to "MHL",
        "Macedonia, Former Yugoslav Republic of" to "MKD",
        "Mali" to "MLI",
        "Malta" to "MLT",
        "Myanmar" to "MMR",
        "Montenegro" to "MNE",
        "Mongolia" to "MNG",
        "Northern Mariana Islands" to "MNP",
        "Mozambique" to "MOZ",
        "Mauritania" to "MRT",
        "Montserrat" to "MSR",
        "Martinique" to "MTQ",
        "Mauritius" to "MUS",
        "Malawi" to "MWI",
        "Malaysia" to "MYS",
        "Mayotte" to "MYT",
        "Namibia" to "NAM",
        "New Caledonia" to "NCL",
        "Niger" to "NER",
        "Norfolk Island" to "NFK",
        "Nigeria" to "NGA",
        "Nicaragua" to "NIC",
        "Niue" to "NIU",
        "Netherlands" to "NLD",
        "Norway" to "NOR",
        "Nepal" to "NPL",
        "Nauru" to "NRU",
        "New Zealand" to "NZL",
        "Oman" to "OMN",
        "Pakistan" to "PAK",
        "Panama" to "PAN",
        "Pitcairn Islands" to "PCN",
        "Peru" to "PER",
        "Philippines" to "PHL",
        "Palau" to "PLW",
        "Papua New Guinea" to "PNG",
        "Poland" to "POL",
        "Puerto Rico" to "PRI",
        "Dem. Rep. Korea" to "PRK",
        "Portugal" to "PRT",
        "Paraguay" to "PRY",
        "Palestine" to "PSE",
        "French Polynesia" to "PYF",
        "Qatar" to "QAT",
        "Reunion" to "REU",
        "Romania" to "ROU",
        "Russian Federation" to "RUS",
        "Rwanda" to "RWA",
        "Saudi Arabia" to "SAU",
        "Sudan" to "SDN",
        "Senegal" to "SEN",
        "Singapore" to "SGP",
        "South Georgia and South Sandwich Islands" to "SGS",
        "Saint Helena" to "SHN",
        "Svalbard and Jan Mayen" to "SJM",
        "Solomon Islands" to "SLB",
        "Sierra Leone" to "SLE",
        "El Salvador" to "SLV",
        "San Marino" to "SMR",
        "Somalia" to "SOM",
        "Saint Pierre and Miquelon" to "SPM",
        "Serbia" to "SRB",
        "South Sudan" to "SSD",
        "Sao Tome and Principe" to "STP",
        "Suriname" to "SUR",
        "Slovakia" to "SVK",
        "Slovenia" to "SVN",
        "Sweden" to "SWE",
        "Swaziland" to "SWZ",
        "Sint Maarten" to "SXM",
        "Seychelles" to "SYC",
        "Syria" to "SYR",
        "United States" to "USA",
        "Turks and Caicos Islands" to "TCA",
        "Chad" to "TCD",
        "Togo" to "TGO",
        "Thailand" to "THA",
        "Tajikistan" to "TJK",
        "Turkmenistan" to "TKM",
        "Timor-Leste" to "TLS",
        "Tonga" to "TON",
        "Trinidad and Tobago" to "TTO",
        "Tunisia" to "TUN",
        "Turkey" to "TUR",
        "Tuvalu" to "TUV",
        "Taiwan" to "TWN",
        "Tanzania" to "TZA",
        "Uganda" to "UGA",
        "Ukraine" to "UKR",
        "United States Minor Outlying Islands" to "UMI",
        "Uruguay" to "URY",
        "Uzbekistan" to "UZB",
        "Vatican" to "VAT",
        "Saint Vincent and the Grenadines" to "VCT",
        "Venezuela" to "VEN",
        "British Virgin Islands" to "VGB",
        "United States Virgin Islands" to "VIR",
        "Vietnam" to "VNM",
        "Vanuatu" to "VUT",
        "Wallis and Futuna Islands" to "WLF",
        "Samoa" to "WSM",
        "Yemen" to "YEM",
        "South Africa" to "ZAF",
        "Zambia" to "ZMB",
        "Zimbabwe" to "ZWE"
    )

    val fireCountryLatLngMap: Map<String, LatLng> = mapOf(
        "ABW" to LatLng(12.52111, -69.968338),
        "AFG" to LatLng(33.93911, 67.709953),
        "AGO" to LatLng(-11.202692, 17.873887),
        "AIA" to LatLng(18.220554, -63.068615),
        "ALA" to LatLng(60.1785, 19.9156),
        "ALB" to LatLng(41.153332, 20.168331),
        "AND" to LatLng(42.546245, 1.601554),
        "ARE" to LatLng(23.424076, 53.847818),
        "ARG" to LatLng(-38.416097, -63.616672),
        "ARM" to LatLng(40.069099, 45.038189),
        "ASM" to LatLng(-14.270972, -170.132217),
        "ATA" to LatLng(-75.250973, -0.071389),
        "ATF" to LatLng(49.2804, 69.3486),
        "ATG" to LatLng(17.060816, -61.796428),
        "AUS" to LatLng(-25.274398, 133.775136),
        "AUT" to LatLng(47.516231, 14.550072),
        "AZE" to LatLng(40.143105, 47.576927),
        "BDI" to LatLng(-3.373056, 29.918886),
        "BEL" to LatLng(50.503887, 4.469936),
        "BEN" to LatLng(9.30769, 2.315834),
        "BFA" to LatLng(12.238333, -1.561593),
        "BGD" to LatLng(23.684994, 90.356331),
        "BGR" to LatLng(42.733883, 25.48583),
        "BHR" to LatLng(25.930414, 50.637772),
        "BHS" to LatLng(25.03428, -77.39628),
        "BIH" to LatLng(43.915886, 17.679076),
        "BLM" to LatLng(17.9000, 62.8333),
        "BLR" to LatLng(53.709807, 27.953389),
        "BLZ" to LatLng(17.189877, -88.49765),
        "BMU" to LatLng(32.321384, -64.75737),
        "BOL" to LatLng(-16.290154, -63.588653),
        "BRA" to LatLng(-14.235004, -51.92528),
        "BRB" to LatLng(13.193887, -59.543198),
        "BRN" to LatLng(4.535277, 114.727669),
        "BTN" to LatLng(27.514162, 90.433601),
        "BWA" to LatLng(-22.328474, 24.684866),
        "CAF" to LatLng(6.611111, 20.939444),
        "CAN" to LatLng(56.130366, -106.346771),
        "CHE" to LatLng(46.818188, 8.227512),
        "CHL" to LatLng(-35.675147, -71.542969),
        "CHN" to LatLng(35.86166, 104.195397),
        "CIV" to LatLng(7.539989, -5.54708),
        "CMR" to LatLng(7.369722, 12.354722),
        "COD" to LatLng(-4.038333, 21.758664),
        "COG" to LatLng(-0.228021, 15.827659),
        "COK" to LatLng(-21.236736, -159.777671),
        "COL" to LatLng(4.570868, -74.297333),
        "COM" to LatLng(-11.875001, 43.872219),
        "CPV" to LatLng(16.002082, -24.013197),
        "CRI" to LatLng(9.748917, -83.753428),
        "CUB" to LatLng(21.521757, -77.781167),
        "CUW" to LatLng(12.1696, 68.9900),
        "CYM" to LatLng(19.513469, -80.566956),
        "CYP" to LatLng(35.126413, 33.429859),
        "CZE" to LatLng(49.817492, 15.472962),
        "DEU" to LatLng(51.165691, 10.451526),
        "DJI" to LatLng(11.825138, 42.590275),
        "DMA" to LatLng(15.414999, -61.370976),
        "DNK" to LatLng(56.26392, 9.501785),
        "DOM" to LatLng(18.735693, -70.162651),
        "DZA" to LatLng(28.033886, 1.659626),
        "ECU" to LatLng(-1.831239, -78.183406),
        "EGY" to LatLng(26.820553, 30.802498),
        "ERI" to LatLng(15.179384, 39.782334),
        "ESP" to LatLng(40.463667, -3.74922),
        "EST" to LatLng(58.595272, 25.013607),
        "ETH" to LatLng(9.145, 40.489673),
        "FIN" to LatLng(61.92411, 25.748151),
        "FJI" to LatLng(-16.578193, 179.414413),
        "FLK" to LatLng(-51.796253, -59.523613),
        "FRA" to LatLng(46.227638, 2.213749),
        "FRO" to LatLng(61.892635, -6.911806),
        "FSM" to LatLng(7.425554, 150.550812),
        "GAB" to LatLng(-0.803689, 11.609444),
        "GBR" to LatLng(55.378051, -3.435973),
        "GEO" to LatLng(42.315407, 43.356892),
        "GGY" to LatLng(49.465691, -2.585278),
        "GHA" to LatLng(7.946527, -1.023194),
        "GIB" to LatLng(36.137741, -5.345374),
        "GIN" to LatLng(9.945587, -9.696645),
        "GLP" to LatLng(16.995971, -62.067641),
        "GMB" to LatLng(13.443182, -15.310139),
        "GNB" to LatLng(9.945587, -9.696645),
        "GNQ" to LatLng(1.650801, 10.267895),
        "GRC" to LatLng(39.074208, 21.824312),
        "GRD" to LatLng(12.262776, -61.604171),
        "GRL" to LatLng(71.706936, -42.604303),
        "GTM" to LatLng(15.783471, -90.230759),
        "GUF" to LatLng(3.933889, -53.125782),
        "GUM" to LatLng(13.444304, 144.793731),
        "GUY" to LatLng(4.860416, -58.93018),
        "HKG" to LatLng(22.396428, 114.109497),
        "HMD" to LatLng(-53.08181, 73.504158),
        "HND" to LatLng(15.199999, -86.241905),
        "HRV" to LatLng(45.1, 15.2),
        "HTI" to LatLng(18.971187, -72.285215),
        "HUN" to LatLng(47.162494, 19.503304),
        "IDN" to LatLng(-0.789275, 113.921327),
        "IMN" to LatLng(54.236107, -4.548056),
        "IND" to LatLng(20.593684, 78.96288),
        "IOT" to LatLng(-6.343194, 71.876519),
        "IRL" to LatLng(53.41291, -8.24389),
        "IRN" to LatLng(32.427908, 53.688046),
        "IRQ" to LatLng(33.223191, 43.679291),
        "ISL" to LatLng(64.963051, -19.020835),
        "ISR" to LatLng(31.046051, 34.851612),
        "ITA" to LatLng(41.87194, 12.56738),
        "JAM" to LatLng(18.109581, -77.297508),
        "JEY" to LatLng(49.214439, -2.13125),
        "JOR" to LatLng(30.585164, 36.238414),
        "JPN" to LatLng(36.204824, 138.252924),
        "KAZ" to LatLng(48.019573, 66.923684),
        "KEN" to LatLng(-0.023559, 37.906193),
        "KGZ" to LatLng(41.20438, 74.766098),
        "KHM" to LatLng(12.565679, 104.990963),
        "KIR" to LatLng(-3.370417, -168.734039),
        "KNA" to LatLng(17.357822, -62.782998),
        "KOR" to LatLng(35.907757, 127.766922),
        "KOS" to LatLng(42.602636, 20.902977),
        "KWT" to LatLng(29.31166, 47.481766),
        "LAO" to LatLng(19.85627, 102.495496),
        "LBN" to LatLng(33.854721, 35.862285),
        "LBR" to LatLng(6.428055, -9.429499),
        "LBY" to LatLng(26.3351, 17.228331),
        "LCA" to LatLng(13.909444, -60.978893),
        "LIE" to LatLng(47.166, 9.555373),
        "LKA" to LatLng(7.873054, 80.771797),
        "LSO" to LatLng(-29.609988, 28.233608),
        "LTU" to LatLng(55.169438, 23.881275),
        "LUX" to LatLng(49.815273, 6.129583),
        "LVA" to LatLng(56.879635, 24.603189),
        "MAC" to LatLng(22.198745, 113.543873),
        "MAF" to LatLng(18.0708, 63.0501),
        "MAR" to LatLng(31.791702, -7.09262),
        "MCO" to LatLng(43.750298, 7.412841),
        "MDA" to LatLng(47.411631, 28.369885),
        "MDG" to LatLng(-18.766947, 46.869107),
        "MDV" to LatLng(3.202778, 73.22068),
        "MEX" to LatLng(23.634501, -102.552784),
        "MHL" to LatLng(7.131474, 171.184478),
        "MKD" to LatLng(41.608635, 21.745275),
        "MLI" to LatLng(17.570692, -3.996166),
        "MLT" to LatLng(35.937496, 14.375416),
        "MMR" to LatLng(21.913965, 95.956223),
        "MNE" to LatLng(42.708678, 19.37439),
        "MNG" to LatLng(46.862496, 103.846656),
        "MNP" to LatLng(17.33083, 145.38469),
        "MOZ" to LatLng(-18.665695, 35.529562),
        "MRT" to LatLng(21.00789, -10.940835),
        "MSR" to LatLng(16.742498, -62.187366),
        "MTQ" to LatLng(14.641528, -61.024174),
        "MUS" to LatLng(-20.348404, 57.552152),
        "MWI" to LatLng(-13.254308, 34.301525),
        "MYS" to LatLng(4.210484, 101.975766),
        "MYT" to LatLng(-12.8275, 45.166244),
        "NAM" to LatLng(-22.95764, 18.49041),
        "NCL" to LatLng(-20.904305, 165.618042),
        "NER" to LatLng(17.607789, 8.081666),
        "NFK" to LatLng(-29.040835, 167.954712),
        "NGA" to LatLng(9.081999, 8.675277),
        "NIC" to LatLng(12.865416, -85.207229),
        "NIU" to LatLng(-19.054445, -169.867233),
        "NLD" to LatLng(52.132633, 5.291266),
        "NOR" to LatLng(60.472024, 8.468946),
        "NPL" to LatLng(28.394857, 84.124008),
        "NRU" to LatLng(-0.522778, 166.931503),
        "NZL" to LatLng(-40.900557, 174.885971),
        "OMN" to LatLng(21.512583, 55.923255),
        "PAK" to LatLng(30.375321, 69.345116),
        "PAN" to LatLng(8.537981, -80.782127),
        "PCN" to LatLng(-24.703615, -127.439308),
        "PER" to LatLng(-9.189967, -75.015152),
        "PHL" to LatLng(12.879721, 121.774017),
        "PLW" to LatLng(7.51498, 134.58252),
        "PNG" to LatLng(-6.314993, 143.95555),
        "POL" to LatLng(51.919438, 19.145136),
        "PRI" to LatLng(18.220833, -66.590149),
        "PRK" to LatLng(40.339852, 127.510093),
        "PRT" to LatLng(39.399872, -8.224454),
        "PRY" to LatLng(-23.442503, -58.443832),
        "PSE" to LatLng(31.952162, 35.233154),
        "PYF" to LatLng(-17.679742, -149.406843),
        "QAT" to LatLng(25.354826, 51.183884),
        "REU" to LatLng(-21.115141, 55.536384),
        "ROU" to LatLng(45.943161, 24.96676),
        "RUS" to LatLng(61.52401, 105.318756),
        "RWA" to LatLng(-1.940278, 29.873888),
        "SAU" to LatLng(23.885942, 45.079162),
        "SDN" to LatLng(12.862807, 30.217636),
        "SEN" to LatLng(14.497401, -14.452362),
        "SGP" to LatLng(1.352083, 103.819836),
        "SGS" to LatLng(-54.429579, -36.587909),
        "SHN" to LatLng(-24.143474, -10.030696),
        "SJM" to LatLng(77.553604, 23.670272),
        "SLB" to LatLng(-9.64571, 160.156194),
        "SLE" to LatLng(8.460555, -11.779889),
        "SLV" to LatLng(13.794185, -88.89653),
        "SMR" to LatLng(43.94236, 12.457777),
        "SOM" to LatLng(5.152149, 46.199616),
        "SPM" to LatLng(46.941936, -56.27111),
        "SRB" to LatLng(44.016521, 21.005859),
        "SSD" to LatLng(12.862807, 30.217636),
        "STP" to LatLng(0.18636, 6.613081),
        "SUR" to LatLng(3.919305, -56.027783),
        "SVK" to LatLng(48.669026, 19.699024),
        "SVN" to LatLng(46.151241, 14.995463),
        "SWE" to LatLng(60.128161, 18.643501),
        "SWZ" to LatLng(-26.522503, 31.465866),
        "SXM" to LatLng(18.0425, 63.0548),
        "SYC" to LatLng(-4.679574, 55.491977),
        "SYR" to LatLng(34.802075, 38.996815),
        "TCA" to LatLng(21.694025, -71.797928),
        "TCD" to LatLng(15.454166, 18.732207),
        "TGO" to LatLng(8.619543, 0.824782),
        "THA" to LatLng(15.870032, 100.992541),
        "TJK" to LatLng(38.861034, 71.276093),
        "TKM" to LatLng(38.969719, 59.556278),
        "TLS" to LatLng(-8.874217, 125.727539),
        "TON" to LatLng(-21.178986, -175.198242),
        "TTO" to LatLng(10.691803, -61.222503),
        "TUN" to LatLng(33.886917, 9.537499),
        "TUR" to LatLng(38.963745, 35.243322),
        "TUV" to LatLng(-7.109535, 177.64933),
        "TWN" to LatLng(23.69781, 120.960515),
        "TZA" to LatLng(-6.369028, 34.888822),
        "UGA" to LatLng(1.373333, 32.290275),
        "UKR" to LatLng(48.379433, 31.16558),
        "UMI" to LatLng(19.2823, 166.6470),
        "URY" to LatLng(-32.522779, -55.765835),
        "USA" to LatLng(37.09024, -95.712891),
        "UZB" to LatLng(41.377491, 64.585262),
        "VAT" to LatLng(41.902916, 12.453389),
        "VCT" to LatLng(12.984305, -61.287228),
        "VEN" to LatLng(6.42375, -66.58973),
        "VGB" to LatLng(18.420695, -64.639968),
        "VIR" to LatLng(18.335765, -64.896335),
        "VNM" to LatLng(14.058324, 108.277199),
        "VUT" to LatLng(-15.376706, 166.959158),
        "WLF" to LatLng(-13.768752, -177.156097),
        "WSM" to LatLng(-13.759029, -172.104629),
        "YEM" to LatLng(15.552727, 48.516388),
        "ZAF" to LatLng(-30.559482, 22.937506),
        "ZMB" to LatLng(-13.133897, 27.849332),
        "ZWE" to LatLng(-19.015438, 29.154857)
    )
}
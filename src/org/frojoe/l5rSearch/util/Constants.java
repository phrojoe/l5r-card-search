package org.frojoe.l5rSearch.util;

import java.util.HashMap;
import java.util.Map;

import org.frojoe.l5rSearch.R;

public class Constants {

    //gesture constants
    public static final int SWIPE_MAX_OFF_PATH = 200
                           , SWIPE_MIN_DISTANCE = 40
                           , SWIPE_THRESHOLD_VELOCITY = 60;

	public static final int MIN_BYTES = 314573; //minimum bytes for card image

	public static final String APP_NAME = "L5RCardSearch";

    /*
     * Intent names
     */
    public static final String
        INTENT_CARDS = "cards"
        , INTENT_POS = "pos"
        , INTENT_CARD = "card"
        , INTENT_IMGURL = "imgurl"
        , INTENT_URLLAUNCH = "urllaunch"
        , INTENT_CARDTITLE = "cardtitle"
        ;

	/*
	 * Oracle Base URL
	 */
	public static final String BASE_URL = "http://imperialassembly.com/oracle/";

	/*
	 * Oracle POST URLs
	 */
	public static final String
		DO_SEARCH_URL = BASE_URL + "dosearch"
		, DO_CARD_URL = BASE_URL + "docard"
		;

	/*
	 * Oracle GET URls
	 */
	public static final String SHOW_IMAGE_URL = BASE_URL + "showimage";

	/*
	 * Oracle Search Params
	 */
	public static final String
		CARD_NAME = "search_13"
	    , CARD_TYPE_P = "search_sel_14[]"
	    , FORCE_LOW = "search_start_1"
	    , FORCE_HIGH = "search_end_1"
	    , CHI_LOW = "search_start_2"
	    , CHI_HIGH = "search_end_2"
	    , HR_LOW = "search_start_3"
	    , HR_HIGH = "search_end_3"
	    , GC_LOW = "search_start_5"
	    , GC_HIGH = "search_end_5"
	    , PH_LOW = "search_start_4"
	    , PH_HIGH = "search_end_4"
	    , PS_LOW = "search_start_54"
	    , PS_HIGH = "search_end_54"
	    , GP_LOW = "search_start_55"
	    , GP_HIGH = "search_end_55"
	    , SFH_LOW = "search_start_56"
	    , SFH_HIGH = "search_end_56"
	    , CLAN = "search_sel_12[]"
	    , KEYWORDS = "search_7"
	    , CARD_TEXT_P = "search_15"
	    , FV_LOW = "search_start_6"
	    , FV_HIGH = "search_end_6"
	    , SET = "search_sel_35[]"
	    , RARITY = "search_sel_38[]"
	    , LEGALITY = "search_sel_10[]"
	    , FLAVOR_TEXT = "search_31"
	    , STORYLINE_CREDIT = "search_32"
	    , ARTIST = "search_17"
	    , ERRATA = "search_t_11"
        , BANNED = "search_t_65"
        , MRP = "search_t_66"
	    , CARD_NUMBER = "search_26"
	    , LEGAL_DATE = "search_62"
	    , DECK = "search_sel_60[]"
	    , NOTES = "search_36"
        , SPEC_NOTES = "search_37"
		, PAGE_NUMBER = "page"
		;

	/*
	 * View to search param map
	 */
	public static final Map<Integer,String> VIEW_PARAM_MAP =
			new HashMap<Integer,String>();
	static {
		VIEW_PARAM_MAP.put(R.id.card_search, CARD_NAME);
		VIEW_PARAM_MAP.put(R.id.keywords_search, KEYWORDS);
		VIEW_PARAM_MAP.put(R.id.card_text_search, CARD_TEXT_P);
		VIEW_PARAM_MAP.put(R.id.card_type_search, CARD_TYPE_P);
		VIEW_PARAM_MAP.put(R.id.clan_search, CLAN);
		VIEW_PARAM_MAP.put(R.id.set_search, SET);
		VIEW_PARAM_MAP.put(R.id.rarity_search, RARITY);
		VIEW_PARAM_MAP.put(R.id.legality_search, LEGALITY);
		VIEW_PARAM_MAP.put(R.id.flavor_text_search, FLAVOR_TEXT);
		VIEW_PARAM_MAP.put(R.id.storyline_credit_search, STORYLINE_CREDIT);
		VIEW_PARAM_MAP.put(R.id.artist_search, ARTIST);
		VIEW_PARAM_MAP.put(R.id.card_number_search, CARD_NUMBER);
		VIEW_PARAM_MAP.put(R.id.legal_date_search, LEGAL_DATE);
		VIEW_PARAM_MAP.put(R.id.deck_search, DECK);
		VIEW_PARAM_MAP.put(R.id.notes_search, NOTES);
        VIEW_PARAM_MAP.put(R.id.spec_notes_search, SPEC_NOTES);
		VIEW_PARAM_MAP.put(R.id.errata_cb, ERRATA);
        VIEW_PARAM_MAP.put(R.id.mrp_cb, MRP);
        VIEW_PARAM_MAP.put(R.id.banned_cb, BANNED);
	}

	/*
	 * Basic EditTexts
	 */
	public static final int[] BASIC_SEARCH_EDIT_TEXTS = new int[] {
		R.id.card_search
		, R.id.keywords_search
		, R.id.card_text_search
	};

	/*
	 * Advanced EditTexts
	 */
	public static final int[] ADV_SEARCH_EDIT_TEXTS = new int[] {
		R.id.flavor_text_search
		, R.id.storyline_credit_search
		, R.id.artist_search
		, R.id.card_number_search
		, R.id.legal_date_search
		, R.id.notes_search
        , R.id.spec_notes_search
	};

	/*
	 * Basic Spinners
	 */
	public static final int[] BASIC_SEARCH_SPINNERS = new int[] {
		R.id.card_type_search
		, R.id.clan_search
		, R.id.set_search
		, R.id.rarity_search
		, R.id.legality_search
	};

    /*
     * Checkboxes
     */
    public static final int[] ADV_SEARCH_CBS = new int[] {
        R.id.errata_cb
        , R.id.mrp_cb
        , R.id.banned_cb
    };

	/*
	 * Adv Spinners
	 */
	public static final int[] ADV_SEARCH_SPINNERS = new int[] {
		R.id.deck_search
	};

	public static final Map<Integer,String[]> VIEW_MULTI_PARAM_MAP =
			new HashMap<Integer,String[]>();
	static {
		VIEW_MULTI_PARAM_MAP.put(R.id.force_range_bar,
				new String[]{FORCE_LOW, FORCE_HIGH});
		VIEW_MULTI_PARAM_MAP.put(R.id.chi_range_bar,
				new String[]{CHI_LOW, CHI_HIGH});
		VIEW_MULTI_PARAM_MAP.put(R.id.hr_range_bar,
				new String[]{HR_LOW, HR_HIGH});
		VIEW_MULTI_PARAM_MAP.put(R.id.gc_range_bar,
				new String[]{GC_LOW, GC_HIGH});
		VIEW_MULTI_PARAM_MAP.put(R.id.ph_range_bar,
				new String[]{PH_LOW, PH_HIGH});
		VIEW_MULTI_PARAM_MAP.put(R.id.ps_range_bar,
				new String[]{PS_LOW, PS_HIGH});
		VIEW_MULTI_PARAM_MAP.put(R.id.gp_range_bar,
				new String[]{GP_LOW, GP_HIGH});
		VIEW_MULTI_PARAM_MAP.put(R.id.sfh_range_bar,
				new String[]{SFH_LOW, SFH_HIGH});
		VIEW_MULTI_PARAM_MAP.put(R.id.fv_range_bar,
				new String[]{FV_LOW, FV_HIGH});
	}

	/*
	 * RangeSeekBar to CheckBox maps
	 */
	public static final int CB_IDX = 0;
	public static final int MIN_IDX = 1;
	public static final int MAX_IDX = 2;
	public static final Map<Integer,Integer[]> SEEKBAR_GROUP_MAP =
			new HashMap<Integer,Integer[]>();
	static {
		SEEKBAR_GROUP_MAP.put(R.id.force_range_bar,
				new Integer[] { R.id.force_range_bar_cb,
							R.id.force_range_bar_min_text,
							R.id.force_range_bar_max_text});
		SEEKBAR_GROUP_MAP.put(R.id.chi_range_bar,
				new Integer[] { R.id.chi_range_bar_cb,
							R.id.chi_range_bar_min_text,
							R.id.chi_range_bar_max_text});
		SEEKBAR_GROUP_MAP.put(R.id.hr_range_bar,
				new Integer[] { R.id.hr_range_bar_cb,
							R.id.hr_range_bar_min_text,
							R.id.hr_range_bar_max_text});
		SEEKBAR_GROUP_MAP.put(R.id.gc_range_bar,
				new Integer[] { R.id.gc_range_bar_cb,
							R.id.gc_range_bar_min_text,
							R.id.gc_range_bar_max_text});
		SEEKBAR_GROUP_MAP.put(R.id.ph_range_bar,
				new Integer[] { R.id.ph_range_bar_cb,
							R.id.ph_range_bar_min_text,
							R.id.ph_range_bar_max_text});
		SEEKBAR_GROUP_MAP.put(R.id.ps_range_bar,
				new Integer[] { R.id.ps_range_bar_cb,
							R.id.ps_range_bar_min_text,
							R.id.ps_range_bar_max_text});
		SEEKBAR_GROUP_MAP.put(R.id.gp_range_bar,
				new Integer[] { R.id.gp_range_bar_cb,
							R.id.gp_range_bar_min_text,
							R.id.gp_range_bar_max_text});
		SEEKBAR_GROUP_MAP.put(R.id.sfh_range_bar,
				new Integer[] { R.id.sfh_range_bar_cb,
							R.id.sfh_range_bar_min_text,
							R.id.sfh_range_bar_max_text});
		SEEKBAR_GROUP_MAP.put(R.id.fv_range_bar,
				new Integer[] { R.id.fv_range_bar_cb,
							R.id.fv_range_bar_min_text,
							R.id.fv_range_bar_max_text});
	}

	/*
	 * Default selections for search spinners
	 */
	public static final String
		CARD_TYPE_DEFAULT = "Card Type"
		, CLAN_DEFAULT = "Clan"
		, SET_DEFAULT = "Set"
		, RARITY_DEFAULT = "Rarity"
		, LEGALITY_DEFAULT = "Legality"
		, DECK_DEFAULT = "Deck"
		;

	/*
	 * Spinner to default map
	 */
	public static final Map<Integer,String> SPINNER_DEFAULT_MAP =
			new HashMap<Integer,String>();
	static {
		SPINNER_DEFAULT_MAP.put(R.id.card_type_search, CARD_TYPE_DEFAULT);
		SPINNER_DEFAULT_MAP.put(R.id.clan_search, CLAN_DEFAULT);
		SPINNER_DEFAULT_MAP.put(R.id.set_search, SET_DEFAULT);
		SPINNER_DEFAULT_MAP.put(R.id.rarity_search, RARITY_DEFAULT);
		SPINNER_DEFAULT_MAP.put(R.id.legality_search, LEGALITY_DEFAULT);
		SPINNER_DEFAULT_MAP.put(R.id.deck_search, DECK_DEFAULT);
	}

	/*
	 * Spinner value map
	 */
	public static final Map<String,String> SPINNER_VALUE_MAP =
			new HashMap<String,String>();
	static {
		SPINNER_VALUE_MAP.put("Card Type","Card Type");
		SPINNER_VALUE_MAP.put("Ancestor","Ancestor");
		SPINNER_VALUE_MAP.put("Celestial","Celestial");
		SPINNER_VALUE_MAP.put("Event","Event");
		SPINNER_VALUE_MAP.put("Follower","Follower");
		SPINNER_VALUE_MAP.put("Holding","Holding");
		SPINNER_VALUE_MAP.put("Item","Item");
		SPINNER_VALUE_MAP.put("Other","Other");
		SPINNER_VALUE_MAP.put("Personality","Personality");
		SPINNER_VALUE_MAP.put("Proxy","Proxy");
		SPINNER_VALUE_MAP.put("Region","Region");
		SPINNER_VALUE_MAP.put("Ring","Ring");
		SPINNER_VALUE_MAP.put("Sensei","Sensei");
		SPINNER_VALUE_MAP.put("Spell","Spell");
		SPINNER_VALUE_MAP.put("Strategy","Strategy");
		SPINNER_VALUE_MAP.put("Stronghold","Stronghold");
		SPINNER_VALUE_MAP.put("Wind","Wind");
		SPINNER_VALUE_MAP.put("Clan","Clan");
		SPINNER_VALUE_MAP.put("Brotherhood of Shinsei","Brotherhood of Shinsei");
		SPINNER_VALUE_MAP.put("Crab","Crab");
		SPINNER_VALUE_MAP.put("Crane","Crane");
		SPINNER_VALUE_MAP.put("Dragon","Dragon");
		SPINNER_VALUE_MAP.put("Lion","Lion");
		SPINNER_VALUE_MAP.put("Mantis","Mantis");
		SPINNER_VALUE_MAP.put("Naga","Naga");
		SPINNER_VALUE_MAP.put("Ninja","Ninja");
		SPINNER_VALUE_MAP.put("Phoenix","Phoenix");
		SPINNER_VALUE_MAP.put("Ratling","Ratling");
		SPINNER_VALUE_MAP.put("Scorpion","Scorpion");
		SPINNER_VALUE_MAP.put("Shadowlands","Shadowlands");
		SPINNER_VALUE_MAP.put("Spider","Spider");
		SPINNER_VALUE_MAP.put("Spirit","Spirit");
		SPINNER_VALUE_MAP.put("Toturi\'s Army","Toturi\'s Army");
		SPINNER_VALUE_MAP.put("Unaligned","Unaligned");
		SPINNER_VALUE_MAP.put("Unicorn","Unicorn");
		SPINNER_VALUE_MAP.put("Set","Set");
		SPINNER_VALUE_MAP.put("1,000 Years of Darkness","1,000 Years of Darkness");
        SPINNER_VALUE_MAP.put("A Line in the Sand", "A Line in the Sand");
        SPINNER_VALUE_MAP.put("A Matter of Honor", "A Matter of Honor");
		SPINNER_VALUE_MAP.put("A Perfect Cut","A Perfect Cut");
        SPINNER_VALUE_MAP.put("Aftermath","Aftermath");
		SPINNER_VALUE_MAP.put("Ambition\'s Debt","Ambition\'s Debt");
		SPINNER_VALUE_MAP.put("An Oni\'s Fury","An Oni\'s Fury");
		SPINNER_VALUE_MAP.put("Anvil of Despair","Anvil of Despair");
		SPINNER_VALUE_MAP.put("Battle of Beiden Pass","Battle of Beiden Pass");
		SPINNER_VALUE_MAP.put("Battle of Kyuden Tonbo","Battle of Kyuden Tonbo");
		SPINNER_VALUE_MAP.put("Before the Dawn","Before the Dawn");
		SPINNER_VALUE_MAP.put("Broken Blades","Broken Blades");
		SPINNER_VALUE_MAP.put("Celestial Edition","Celestial Edition");
		SPINNER_VALUE_MAP.put("Celestial Edition 15th Anniversary","Celestial Edition 15th Anniversary");
		SPINNER_VALUE_MAP.put("Code of Bushido","Code of Bushido");
		SPINNER_VALUE_MAP.put("Coils of Madness","Coils of Madness");
		SPINNER_VALUE_MAP.put("Crab vs. Lion","Crab vs. Lion");
		SPINNER_VALUE_MAP.put("Crimson and Jade","Crimson and Jade");
		SPINNER_VALUE_MAP.put("Dark Allies","Dark Allies");
		SPINNER_VALUE_MAP.put("Dawn of the Empire","Dawn of the Empire");
		SPINNER_VALUE_MAP.put("Death at Koten","Death at Koten");
		SPINNER_VALUE_MAP.put("Diamond Edition","Diamond Edition");
		SPINNER_VALUE_MAP.put("Drums of War","Drums of War");
		SPINNER_VALUE_MAP.put("Embers of War","Embers of War");
		SPINNER_VALUE_MAP.put("Emerald Edition","Emerald Edition");
		SPINNER_VALUE_MAP.put("Emperor Edition","Emperor Edition");
        SPINNER_VALUE_MAP.put("Emperor Edition Demo Decks", "Emperor Edition Demo Decks");
		SPINNER_VALUE_MAP.put("Emperor Edition Gempukku","Emperor Edition Gempukku");
		SPINNER_VALUE_MAP.put("Empire at War","Empire at War");
		SPINNER_VALUE_MAP.put("Enemy of My Enemy","Enemy of My Enemy");
		SPINNER_VALUE_MAP.put("Fire and Shadow","Fire and Shadow");
		SPINNER_VALUE_MAP.put("Forbidden Knowledge","Forbidden Knowledge");
		SPINNER_VALUE_MAP.put("Forgotten Legacy","Forgotten Legacy");
        SPINNER_VALUE_MAP.put("Gates of Chaos","Gates of Chaos");
		SPINNER_VALUE_MAP.put("Glory of the Empire","Glory of the Empire");
		SPINNER_VALUE_MAP.put("Gold Edition","Gold Edition");
		SPINNER_VALUE_MAP.put("Heaven and Earth","Heaven and Earth");
		SPINNER_VALUE_MAP.put("Heroes of Rokugan","Heroes of Rokugan");
		SPINNER_VALUE_MAP.put("Hidden City","Hidden City");
		SPINNER_VALUE_MAP.put("Hidden Emperor 1","Hidden Emperor 1");
		SPINNER_VALUE_MAP.put("Hidden Emperor 2","Hidden Emperor 2");
		SPINNER_VALUE_MAP.put("Hidden Emperor 3","Hidden Emperor 3");
		SPINNER_VALUE_MAP.put("Hidden Emperor 4","Hidden Emperor 4");
		SPINNER_VALUE_MAP.put("Hidden Emperor 5","Hidden Emperor 5");
		SPINNER_VALUE_MAP.put("Hidden Emperor 6","Hidden Emperor 6");
		SPINNER_VALUE_MAP.put("Honor and Treachery","Honor and Treachery");
		SPINNER_VALUE_MAP.put("Honor Bound","Honor Bound");
		SPINNER_VALUE_MAP.put("Honor\'s Veil","Honor\'s Veil");
		SPINNER_VALUE_MAP.put("Imperial Edition","Imperial Edition");
        SPINNER_VALUE_MAP.put("Ivory Edition", "Ivory Edition");
		SPINNER_VALUE_MAP.put("Jade Edition","Jade Edition");
		SPINNER_VALUE_MAP.put("Khan\'s Defiance","Khan\'s Defiance");
		SPINNER_VALUE_MAP.put("L5R Experience","L5R Experience");
		SPINNER_VALUE_MAP.put("Lotus Edition","Lotus Edition");
		SPINNER_VALUE_MAP.put("Obsidian Edition","Obsidian Edition");
		SPINNER_VALUE_MAP.put("Oracle of the Void","Oracle of the Void");
		SPINNER_VALUE_MAP.put("Path of Hope","Path of Hope");
		SPINNER_VALUE_MAP.put("Path of the Destroyer","Path of the Destroyer");
		SPINNER_VALUE_MAP.put("Pearl Edition","Pearl Edition");
		SPINNER_VALUE_MAP.put("Pre-Imperial Edition","Pre-Imperial Edition");
		SPINNER_VALUE_MAP.put("Reign of Blood","Reign of Blood");
		SPINNER_VALUE_MAP.put("Rise of the Shogun","Rise of the Shogun");
		SPINNER_VALUE_MAP.put("Samurai Edition","Samurai Edition");
		SPINNER_VALUE_MAP.put("Samurai Edition Banzai","Samurai Edition Banzai");
		SPINNER_VALUE_MAP.put("Scorpion Clan Coup 1","Scorpion Clan Coup 1");
		SPINNER_VALUE_MAP.put("Scorpion Clan Coup 2","Scorpion Clan Coup 2");
		SPINNER_VALUE_MAP.put("Scorpion Clan Coup 3","Scorpion Clan Coup 3");
		SPINNER_VALUE_MAP.put("Second City","Second City");
		SPINNER_VALUE_MAP.put("Seeds of Decay","Seeds of Decay");
		SPINNER_VALUE_MAP.put("Shadowlands","Shadowlands");
		SPINNER_VALUE_MAP.put("Siege of Sleeping Mountain","Siege of Sleeping Mountain");
		SPINNER_VALUE_MAP.put("Soul of the Empire","Soul of the Empire");
		SPINNER_VALUE_MAP.put("Storms Over Matsu Palace","Storms Over Matsu Palace");
		SPINNER_VALUE_MAP.put("Stronger Than Steel","Stronger Than Steel");
		SPINNER_VALUE_MAP.put("Test of Enlightenment","Test of Enlightenment");
		SPINNER_VALUE_MAP.put("Test of the Emerald and Jade Championships","Test of the Emerald and Jade Championships");
        SPINNER_VALUE_MAP.put("The Coming Storm", "The Coming Storm");
		SPINNER_VALUE_MAP.put("The Dark Journey Home","The Dark Journey Home");
		SPINNER_VALUE_MAP.put("The Dead of Winter","The Dead of Winter");
		SPINNER_VALUE_MAP.put("The Fall of Otosan Uchi","The Fall of Otosan Uchi");
		SPINNER_VALUE_MAP.put("The Harbinger","The Harbinger");
		SPINNER_VALUE_MAP.put("The Heaven\'s Will","The Heaven\'s Will");
		SPINNER_VALUE_MAP.put("The Imperial Gift 1","The Imperial Gift 1");
		SPINNER_VALUE_MAP.put("The Imperial Gift 2","The Imperial Gift 2");
		SPINNER_VALUE_MAP.put("The Imperial Gift 3","The Imperial Gift 3");
        SPINNER_VALUE_MAP.put("The New Order", "The New Order");
		SPINNER_VALUE_MAP.put("The Plague War","The Plague War");
		SPINNER_VALUE_MAP.put("The Shadow\'s Embrace","The Shadow\'s Embrace");
		SPINNER_VALUE_MAP.put("The Truest Test","The Truest Test");
		SPINNER_VALUE_MAP.put("The War of Spirits","The War of Spirits");
		SPINNER_VALUE_MAP.put("Time of the Void","Time of the Void");
		SPINNER_VALUE_MAP.put("Tomorrow","Tomorrow");
		SPINNER_VALUE_MAP.put("Top Deck Booster Pack","Top Deck Booster Pack");
		SPINNER_VALUE_MAP.put("Torn Asunder","Torn Asunder");
		SPINNER_VALUE_MAP.put("Training Grounds","Training Grounds");
		SPINNER_VALUE_MAP.put("Training Grounds 2","Training Grounds 2");
		SPINNER_VALUE_MAP.put("War of Honor","War of Honor");
		SPINNER_VALUE_MAP.put("Web of Lies","Web of Lies");
		SPINNER_VALUE_MAP.put("Winds of Change","Winds of Change");
		SPINNER_VALUE_MAP.put("Words and Deeds","Words and Deeds");
		SPINNER_VALUE_MAP.put("Wrath of the Emperor","Wrath of the Emperor");
		SPINNER_VALUE_MAP.put("Promotional–Celestial","Promotional&ndash;Celestial");
		SPINNER_VALUE_MAP.put("Promotional–CWF","Promotional&ndash;CWF");
		SPINNER_VALUE_MAP.put("Promotional–Diamond","Promotional&ndash;Diamond");
		SPINNER_VALUE_MAP.put("Promotional–Emperor","Promotional&ndash;Emperor");
		SPINNER_VALUE_MAP.put("Promotional–Gold","Promotional&ndash;Gold");
		SPINNER_VALUE_MAP.put("Promotional–Imperial","Promotional&ndash;Imperial");
		SPINNER_VALUE_MAP.put("Promotional–Jade","Promotional&ndash;Jade");
		SPINNER_VALUE_MAP.put("Promotional–Lotus","Promotional&ndash;Lotus");
		SPINNER_VALUE_MAP.put("Promotional–Samurai","Promotional&ndash;Samurai");
		SPINNER_VALUE_MAP.put("Rarity","Rarity");
		SPINNER_VALUE_MAP.put("Common","Common");
		SPINNER_VALUE_MAP.put("Fixed","Fixed");
		SPINNER_VALUE_MAP.put("None","None");
		SPINNER_VALUE_MAP.put("Premium","Premium");
		SPINNER_VALUE_MAP.put("Promo","Promo");
		SPINNER_VALUE_MAP.put("Rare","Rare");
		SPINNER_VALUE_MAP.put("Uncommon","Uncommon");
		SPINNER_VALUE_MAP.put("Legality","Legality");
		SPINNER_VALUE_MAP.put("Age of Conquest (Emperor)","Age&nbsp;of&nbsp;Conquest&nbsp;(Emperor)");
        SPINNER_VALUE_MAP.put("A Brother\'s Destiny (Ivory)", "A&nbsp;Brother's&nbsp;Destiny&nbsp;(Ivory&nbsp;Edition)");
		SPINNER_VALUE_MAP.put("Ivory Edition Part 2","Ivory Edition Part 2");
		SPINNER_VALUE_MAP.put("Destroyer War (Celestial)","Destroyer&nbsp;War&nbsp;(Celestial)");
		SPINNER_VALUE_MAP.put("Race for the Throne (Samurai)","Race&nbsp;for&nbsp;the&nbsp;Throne&nbsp;(Samurai)");
		SPINNER_VALUE_MAP.put("Age of Enlightenment (Lotus)","Age&nbsp;of&nbsp;Enlightenment&nbsp;(Lotus)");
		SPINNER_VALUE_MAP.put("Rain of Blood (Diamond)","Rain&nbsp;of&nbsp;Blood&nbsp;(Diamond)");
		SPINNER_VALUE_MAP.put("Four Winds (Gold)","Four&nbsp;Winds&nbsp;(Gold)");
		SPINNER_VALUE_MAP.put("Hidden Emperor (Jade)","Hidden&nbsp;Emperor&nbsp;(Jade)");
		SPINNER_VALUE_MAP.put("Clan Wars (Imperial)","Clan&nbsp;Wars&nbsp;(Imperial)");
		SPINNER_VALUE_MAP.put("Not Legal (Proxy)","Not&nbsp;Legal&nbsp;(Proxy)");
		SPINNER_VALUE_MAP.put("Deck","Deck");
		SPINNER_VALUE_MAP.put("Pre-Game","Pre-Game");
		SPINNER_VALUE_MAP.put("Dynasty","Dynasty");
		SPINNER_VALUE_MAP.put("Fate","Fate");
		SPINNER_VALUE_MAP.put("Other","Other");
	}

	/*
	 * Oracle HTML Items
	 */
	public static final String
		CARD_LINE = "cardline"
		, CARD_TEXT = "searchresults"
		, CARD_LINK = "cardlookup"
		, TITLE = "l5rfont"
		, I_TITLE = "greenhead"
		, SET_RARITY = "setrarity"
		, CARD_TYPE = "cardtype"
		, FORCE = "smforce"
		, CHI = "smichi"
		, HONOR = "smhr"
		, GOLD = "smgc"
		, PERSONAL_HONOR = "smph"
		, FOCUS = "smfo"
		, MATCHES = "listingmatches"
		;

	/*
	 * Array of TextView ids with tooltips
	 */
	public static final Map<Integer,Integer> TEXT_VIEW_TOOLTIPS_MAP =
			new HashMap<Integer,Integer>();
			static{
				TEXT_VIEW_TOOLTIPS_MAP.put(
						R.id.force_range_bar_label,R.string.force_tooltip);
				TEXT_VIEW_TOOLTIPS_MAP.put(
						R.id.chi_range_bar_label,R.string.chi_tooltip);
				TEXT_VIEW_TOOLTIPS_MAP.put(
						R.id.hr_range_bar_label,R.string.hr_tooltip);
				TEXT_VIEW_TOOLTIPS_MAP.put(
						R.id.gc_range_bar_label,R.string.gc_tooltip);
				TEXT_VIEW_TOOLTIPS_MAP.put(
						R.id.ph_range_bar_label,R.string.ph_tooltip);
				TEXT_VIEW_TOOLTIPS_MAP.put(
						R.id.ps_range_bar_label,R.string.ps_tooltip);
				TEXT_VIEW_TOOLTIPS_MAP.put(
						R.id.gp_range_bar_label,R.string.gp_tooltip);
				TEXT_VIEW_TOOLTIPS_MAP.put(
						R.id.sfh_range_bar_label,R.string.sfh_tooltip);
				TEXT_VIEW_TOOLTIPS_MAP.put(
						R.id.fv_range_bar_label,R.string.fv_tooltip);
			}

	/*
	 * Advanced search ranges
	 */
	public static final int
		FORCE_MIN = 0
		, FORCE_MAX = 25
		, CHI_MIN = 0
		, CHI_MAX = 20
		, HR_MIN = 0
		, HR_MAX = 25
		;

	/*
	 * new font indicators
	 */
	public static final String
		L5R_FONT_START = "l5rfontstart"
		, L5R_FONT_END = "l5rfontend"
		, GOLD_ICON_START = "goldIconStart"
		, GOLD_ICON_END = "goldIconEnd"
		;

	public static final String[] SPAN_INDICATORS = {
		L5R_FONT_START, L5R_FONT_END, GOLD_ICON_START, GOLD_ICON_END
	};

	/*
	 * Card data order
	 * NOT USED yet...
	 */
	public static final String[] order =  {
		"Card Title","Set","Force/Chi","Text","HR/GC/PH","Keywords"
	};
}

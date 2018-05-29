/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UT;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import JPWord.Data.*;
import JPWord.Data.Filter.*;
import JPWord.Synchronizer.IController;
import JPWord.Synchronizer.ILogging;
import JPWord.Synchronizer.Method;
import JPWord.Synchronizer.Sync;
import JPWord.Synchronizer.Log;
import SqliteEngine_JDBC.*;
import java.io.File;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.runners.MethodSorters;

/**
 *
 * @author u0151316
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JPLibUT {

    public JPLibUT() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @Test
    public void test_000_InitDB() throws Exception {
        File file = new File("TEST.db");
        file.delete();

        String Test_DB_Name = "TEST";
        Database.getInstance().initialize(
                new File("").getAbsolutePath(), new JDBC_SQLEngine());
        IWordDictionary dict = Database.getInstance().loadDictionary(Test_DB_Name);
        assertNull(dict);
        dict = Database.getInstance().createDictionary(Test_DB_Name);
        assertNotNull(dict);
        Database.getInstance().closeDictionary(dict);
        assertEquals(Database.getInstance().getDictList().size(), 1);

        Database.getInstance().initialize(
                new File("").getAbsolutePath(), new JDBC_SQLEngine());
        assertEquals(Database.getInstance().getDictList().size(), 1);
    }

    @Test
    public void test_020_DBReload() throws Exception {
        File file = new File("NEW_TEST.db");
        file.delete();

        String Test_DB_Name = "NEW_TEST";
        Database.getInstance().initialize(
                new File("").getAbsolutePath(), new JDBC_SQLEngine());
        IWordDictionary dict = Database.getInstance().loadDictionary(Test_DB_Name);
        assertNull(dict);
        dict = Database.getInstance().createDictionary(Test_DB_Name);
        assertNotNull(dict);
        Database.getInstance().closeDictionary(dict);

        IWordDictionary dict_new = Database.getInstance().loadDictionary(Test_DB_Name);
        assertNotNull(dict_new);
        Database.getInstance().closeDictionary(dict_new);
        Database.getInstance().deleteDictionary(dict_new.getName());
    }

    @Test
    public void test_030_DBAddData() throws Exception {
        Database.getInstance().initialize(
                new File("").getAbsolutePath(), new JDBC_SQLEngine());
        Database.getInstance().deleteDictionary("TEST");
        assertEquals(Database.getInstance().getDictList().size(), 0);

        String Test_DB_Name = "TEST";
        Database.getInstance().initialize(
                new File("").getAbsolutePath(), new JDBC_SQLEngine());
        IWordDictionary dict = Database.getInstance().loadDictionary(Test_DB_Name);
        assertNull(dict);
        dict = Database.getInstance().createDictionary(Test_DB_Name);
        assertNotNull(dict);

        IWord word = dict.createWord();
        assertNotNull(word);
        dict.addWord(word);
        assertEquals(dict.getWords().size(), 1);
        word.setContent("CONTENT_A");
        word.setKana("KANA_A");
        word.setTone("A");
        word.setNote("NOTE_A");
        word.setCls(3);
        word.updateSkill(3);
        dict.saveToDB();
        Database.getInstance().closeDictionary(dict);

    }

    @Test
    public void test_040_CheckeAddedData() throws Exception {
        String Test_DB_Name = "TEST";
        Database.getInstance().initialize(
                new File("").getAbsolutePath(), new JDBC_SQLEngine());
        IWordDictionary dict = Database.getInstance().loadDictionary(Test_DB_Name);
        assertNotNull(dict);
        assertEquals(dict.getWords().size(), 1);
        IWord word = dict.getWords().get(0);
        assertEquals(word.getContent(), "CONTENT_A");
        assertEquals(word.getKana(), "KANA_A");
        assertEquals(word.getTone(), "A");
        assertEquals(word.getNote(), "NOTE_A");
        assertEquals(word.getCls(), 3);
        assertEquals(word.getSkill(), 3);
        Calendar now = Calendar.getInstance();
        String newReviewDate = String.format("%04d%02d%02d", now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH));
        assertEquals(newReviewDate, word.getReviewDate());
        Database.getInstance().closeDictionary(dict);
    }

    @Test
    public void test_050_UpdateExistData() throws Exception {
        String Test_DB_Name = "TEST";
        Database.getInstance().initialize(
                new File("").getAbsolutePath(), new JDBC_SQLEngine());
        IWordDictionary dict = Database.getInstance().loadDictionary(Test_DB_Name);
        assertNotNull(dict);
        assertEquals(dict.getWords().size(), 1);
        IWord word = dict.getWords().get(0);
        word.setKana("KANA_A_CHANGED");
        IMeaning mean = dict.createMeaning();
        mean.setInCHS("MEAN_CHS_A");
        mean.setInJP("MEAN_JP_A");
        mean.setType("T_A");
        IExample example = dict.createExample();
        example.setExampleInCHS("EX_CHS");
        example.setExampleInJP("EX_JP");
        IExample example2 = dict.createExample();
        example2.setExampleInCHS("EX_CHS_B");
        mean.addExample(example);
        mean.addExample(example2);
        assertEquals(mean.getExamples().size(), 2);
        word.addMeaning(mean);
        IMeaning mean_2 = dict.createMeaning();
        mean_2.setType("T_B");
        mean_2.setInCHS("MEAN_CHS_B");
        word.addMeaning(mean_2);
        assertEquals(word.getMeanings().size(), 2);
        Database.getInstance().closeDictionary(dict);
    }

    @Test
    public void test_060_CheckUpdatedData() throws Exception {
        String Test_DB_Name = "TEST";
        Database.getInstance().initialize(
                new File("").getAbsolutePath(), new JDBC_SQLEngine());
        IWordDictionary dict = Database.getInstance().loadDictionary(Test_DB_Name);
        assertNotNull(dict);
        assertEquals(dict.getWords().size(), 1);
        IWord word = dict.getWords().get(0);
        assertEquals(word.getKana(), "KANA_A_CHANGED");
        assertEquals(word.getMeanings().size(), 2);
        IMeaning mean = word.getMeanings().get(0);
        assertEquals(mean.getType(), "T_A");
        assertEquals(mean.getInCHS(), "MEAN_CHS_A");
        assertEquals(mean.getInJP(), "MEAN_JP_A");
        assertEquals(mean.getExamples().size(), 2);
        IExample example = mean.getExamples().get(0);
        assertEquals(example.getExampleInCHS(), "EX_CHS");
        assertEquals(example.getExampleInJP(), "EX_JP");
        IExample example2 = mean.getExamples().get(1);
        assertEquals(example2.getExampleInCHS(), "EX_CHS_B");
        assertEquals(example2.getExampleInJP(), "");
        IMeaning mean2 = word.getMeanings().get(1);
        assertEquals(mean2.getType(), "T_B");
        assertEquals(mean2.getInCHS(), "MEAN_CHS_B");
        assertEquals(mean2.getInJP(), "");
        Database.getInstance().closeDictionary(dict);
    }

    @Test
    public void test_070_AddMoreDataForFurtherTest() throws Exception {
        String Test_DB_Name = "TEST";
        Database.getInstance().initialize(
                new File("").getAbsolutePath(), new JDBC_SQLEngine());
        IWordDictionary dict = Database.getInstance().loadDictionary(Test_DB_Name);
        assertNotNull(dict);
        assertEquals(dict.getWords().size(), 1);
        for (int i = 0; i < 11; i++) {
            IWord word = dict.createWord();
            word.setContent("CONTENT_" + Integer.toString(i));
            word.setKana("KANA_" + Integer.toString(i));
            word.setTone(Integer.toString(i));
            word.setCls(i);
            word.updateSkill(i);
            dict.addWord(word);
        }
        assertEquals(12, dict.getWords().size());
        Database.getInstance().closeDictionary(dict);
    }

    @Test
    public void test_080_FilterSoftByNumber() throws Exception {
        String Test_DB_Name = "TEST";
        Database.getInstance().initialize(
                new File("").getAbsolutePath(), new JDBC_SQLEngine());
        IWordDictionary dict = Database.getInstance().loadDictionary(Test_DB_Name);
        assertNotNull(dict);
        assertEquals(dict.getWords().size(), 12);
        ItemGroup group = new ItemGroup(dict.getWords());
        List<Integer> values = new LinkedList<>();
        values.add(1);
        values.add(3);
        FilterByInteger filterByInteger = new FilterByInteger(new IIntegerChecker() {
            @Override
            public boolean checkInteger(IWord word, int value) {
                return word.getCls() == value;
            }
        }, values);
        SortByInteger sortByInteger = new SortByInteger(new IIntegerGetter() {
            @Override
            public int getInteger(IWord word) {
                return word.getCls();
            }
        }, true);

        group.sort(sortByInteger, filterByInteger);
        assertEquals(3, group.getCount());
        assertEquals(3, ((IWord) group.next()).getCls());
        assertEquals(3, ((IWord) group.next()).getCls());
        assertEquals(1, ((IWord) group.next()).getCls());
        Database.getInstance().closeDictionary(dict);
    }

    @Test
    public void test_090_Shuffle() throws Exception {
        String Test_DB_Name = "TEST";
        Database.getInstance().initialize(
                new File("").getAbsolutePath(), new JDBC_SQLEngine());
        IWordDictionary dict = Database.getInstance().loadDictionary(Test_DB_Name);
        assertNotNull(dict);
        assertEquals(dict.getWords().size(), 12);
        dict.getWords().get(0).updateSkill(0);
        dict.getWords().get(1).updateSkill(0);
        dict.getWords().get(2).updateSkill(0);
        dict.getWords().get(3).updateSkill(0);
        dict.getWords().get(4).updateSkill(1);
        dict.getWords().get(5).updateSkill(1);
        dict.getWords().get(6).updateSkill(1);
        dict.getWords().get(7).updateSkill(1);
        dict.getWords().get(8).updateSkill(2);
        dict.getWords().get(9).updateSkill(2);
        dict.getWords().get(10).updateSkill(2);
        dict.getWords().get(11).updateSkill(2);

        ItemGroup group = new ItemGroup(dict.getWords());
        SortByInteger sortByInteger = new SortByInteger(new IIntegerGetter() {
            @Override
            public int getInteger(IWord word) {
                return word.getSkill();
            }
        }, true);
        group.sort(sortByInteger);
        assertEquals(12, group.getCount());
        assertEquals(2, ((IWord) group.next()).getSkill());
        assertEquals(2, ((IWord) group.next()).getSkill());
        assertEquals(2, ((IWord) group.next()).getSkill());
        assertEquals(2, ((IWord) group.next()).getSkill());
        assertEquals(1, ((IWord) group.next()).getSkill());

        group.shuffle();
        assertEquals(12, group.getCount());
        List<String> seq1 = new LinkedList<>();
        for (int i = 0; i < group.getCount(); i++) {
            IWord word = (IWord) group.next();
            seq1.add(word.getContent());
        }
        group.shuffle();
        assertEquals(12, group.getCount());
        List<String> seq2 = new LinkedList<>();
        for (int i = 0; i < group.getCount(); i++) {
            IWord word = (IWord) group.next();
            seq2.add(word.getContent());
        }
        assertEquals(seq1.size(), seq2.size());
        String seq1_String = "";
        for (String string : seq1) {
            seq1_String += string;
        }
        String seq2_String = "";
        for (String string : seq2) {
            seq2_String += string;
        }
        assertNotEquals(seq1_String, seq2_String);
        Database.getInstance().closeDictionary(dict);
    }

    @Test
    public void test_100_FilterSoftByText() throws Exception {
        String Test_DB_Name = "TEST";
        Database.getInstance().initialize(
                new File("").getAbsolutePath(), new JDBC_SQLEngine());
        IWordDictionary dict = Database.getInstance().loadDictionary(Test_DB_Name);
        assertNotNull(dict);
        assertEquals(dict.getWords().size(), 12);
//        ItemGroup group = new ItemGroup(dict.getWords());
//        List<Integer> values = new LinkedList<>();
//        values.add(1);
//        values.add(3);
//        FilterByInteger filterByInteger = new FilterByInteger(new IIntegerChecker() {
//            @Override
//            public boolean checkInteger(IWord word, int value) {
//                return word.getCls() == value;
//            }
//        }, values);
//        SortByInteger sortByInteger = new SortByInteger(new IIntegerGetter() {
//            @Override
//            public int getInteger(IWord word) {
//                return word.getCls();
//            }
//        }, true);
//
//        group.sort(sortByInteger, filterByInteger);
//        assertEquals(3, group.getCount());
//        assertEquals(3, ((IWord) group.next()).getCls());
//        assertEquals(3, ((IWord) group.next()).getCls());
//        assertEquals(1, ((IWord) group.next()).getCls());
        Database.getInstance().closeDictionary(dict);
    }

    @Test
    public void test_110_kanaToRoma() throws Exception {
        assertEquals("chuugokujinn", Yin50.getInstance().kanaToRoma("ちゅうごくじん").getString());
        assertEquals("amerikajinn", Yin50.getInstance().kanaToRoma("アメリカじん").getString());
        assertEquals("kurisumasutsurii", Yin50.getInstance().kanaToRoma("クリスマスツリー").getString());
        assertEquals("aidhiikaado", Yin50.getInstance().kanaToRoma("アイディーカード").getString());
        assertEquals("shiidhii", Yin50.getInstance().kanaToRoma("シーディー").getString());
        assertEquals("dhizuniiranndo", Yin50.getInstance().kanaToRoma("ディズニーランド").getString());
        assertEquals("～sann", Yin50.getInstance().kanaToRoma("～さん").getString());
        assertEquals("axtsu", Yin50.getInstance().kanaToRoma("あっ").getString());
        assertEquals("atsu", Yin50.getInstance().kanaToRoma("あつ").getString());
        assertEquals("goorudennwiiku", Yin50.getInstance().kanaToRoma("ゴールデンウィーク").getString());
    }

    @Test
    public void test_120_Tags() throws Exception {

    }

    private void MasterStartStop() throws Exception {
        int num = Thread.activeCount();
        ILogging logging = Sync.getInstance().getLogging();
        IController master = Sync.getInstance().runAsMaster();
        Thread.sleep(200);
        assertEquals(num + 1, Thread.activeCount());
        {
            Log log = logging.pop();
            assertEquals("Server started...", log.what());
            assertEquals(Log.Type.SUCCESS, log.type());
        }
        master.stopWorker();
        Thread.sleep(300);
        {
            Log log = logging.pop();
            assertEquals("Exit by user", log.what());
            assertEquals(Log.Type.FAILURE, log.type());
        }
        assertEquals(num, Thread.activeCount());
    }

    @Test
    public void test_130_MasterStartStop() throws Exception {
        MasterStartStop();
        MasterStartStop();
        MasterStartStop();
        //Sync.getInstance().startAsSlave("", Method.OVERLAP);
    }

    @Test
    public void test_140_Rebase() throws Exception {
        // Master start
        int num = Thread.activeCount();
        ILogging logging = Sync.getInstance().getLogging();
        IController master = Sync.getInstance().runAsMaster();
        Thread.sleep(200);
        assertEquals(num + 1, Thread.activeCount());
        {
            Log log = logging.pop();
            assertEquals("Server started...", log.what());
            assertEquals(Log.Type.SUCCESS, log.type());
        }

        // Slave start
        Sync.getInstance().startAsSlave("TEST_SLAVE", Method.REBASE_FROM_MASTER);
        Thread.sleep(200);
        {
            Log log = logging.pop();
            assertEquals("Start as slave ...", log.what());
            assertEquals(Log.Type.SUCCESS, log.type());
            log = logging.pop();
            assertEquals("Finding master services ...", log.what());
            assertEquals(Log.Type.HARMLESS, log.type());
        }

        // Connected
        {
            Log log = logging.pop();
            assertEquals("Received broadcast", log.what());
            assertEquals(Log.Type.SUCCESS, log.type());
//            log = logging.pop();
//            assertEquals("Connected to master", log.what());
//            assertEquals(Log.Type.SUCCESS, log.type());
        }
        //Sync.getInstance().startAsSlave("", Method.OVERLAP);
    }

    public void SlaveWithoutMaster() throws Exception {
        ILogging logging = Sync.getInstance().getLogging();
        Sync.getInstance().startAsSlave("", Method.OVERLAP);
        Thread.sleep(7000);
        {
            Log log = logging.pop();
            assertEquals("Start as slave ...", log.what());
            assertEquals(Log.Type.SUCCESS, log.type());
            for (int i = 0; i < 5; i++) {
                log = logging.pop();
                assertEquals("Finding master services ...", log.what());
                assertEquals(Log.Type.HARMLESS, log.type());
            }
            log = logging.pop();
            assertEquals("Cannot find master", log.what());
            assertEquals(Log.Type.FAILURE, log.type());
        }
    }

    @Ignore
    public void test_999_SlaveWithoutMaster() throws Exception {
        SlaveWithoutMaster();
        SlaveWithoutMaster();
        SlaveWithoutMaster();
    }
}

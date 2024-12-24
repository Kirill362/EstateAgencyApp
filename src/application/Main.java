package application;
	
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Locale;

import oracle.jdbc.pool.OracleDataSource;


public class Main extends Application {
	private PreparedStatement pstmt;
	private Statement stmt;
	private ResultSet rset;
	private Connection conn;
	private int lastClientId = 1;
    @Override
    public void start(Stage primaryStage) {
    	Realtor[] realtorList = new Realtor[5];
    	Flat[] flatList = new Flat[12];
    	String username = "orcluser";
		String password = "jdbcuser";
		// Connect to a database
		OracleDataSource ods;
		try {
			ods = new OracleDataSource();
			ods.setURL("jdbc:oracle:thin:"+username+"/"+password+"@localhost:1521/xepdb1");
			conn = ods.getConnection();
			stmt = conn.createStatement();
			rset = stmt.executeQuery("SELECT * FROM realtors");
			int s = 0;
			while(rset.next()) {
				realtorList[s] = new Realtor(windows1251_to_utf8(rset.getString(2)), rset.getString(3), Double.parseDouble(rset.getString(4)), rset.getString(5));
				s++;
			}
			s=1;
			int reId = 0;
			rset = stmt.executeQuery("SELECT * FROM flats JOIN addresses ON (id=id_1) JOIN realtors_for_flats ON (id=flat_id)");
			while(rset.next()) {
				if(s%2==1) 
					reId = Integer.parseInt(rset.getString(15));
				else {
					if (rset.getString(13) == null)
						flatList[s/2-1] = new Flat(new Address(windows1251_to_utf8(rset.getString(8)), windows1251_to_utf8(rset.getString(9)), windows1251_to_utf8(rset.getString(10)), Integer.parseInt(rset.getString(11)), Integer.parseInt(rset.getString(12))), Integer.parseInt(rset.getString(4)), Integer.parseInt(rset.getString(3)), Integer.parseInt(rset.getString(2)), rset.getString(6), realtorList[reId-1], realtorList[Integer.parseInt(rset.getString(15))-1]);
					else
						flatList[s/2-1] = new Flat(new Address(windows1251_to_utf8(rset.getString(8)), windows1251_to_utf8(rset.getString(9)), windows1251_to_utf8(rset.getString(10)), Integer.parseInt(rset.getString(11)),Integer.parseInt(rset.getString(13)), Integer.parseInt(rset.getString(12))), Integer.parseInt(rset.getString(4)), Integer.parseInt(rset.getString(3)), Integer.parseInt(rset.getString(2)), rset.getString(6), realtorList[reId-1], realtorList[Integer.parseInt(rset.getString(15))-1]);
				}
				s++;
			}
			rset.close();
			stmt.close();
		} 
		catch (SQLException e) {
			System.out.println(e);
		}
        Group mainMenu = new Group();
        Group realtors = new Group();
        Group flats = new Group();
        Group flats2 = new Group();
        Group goodFlats = new Group();
        Group goodRealtors = new Group();
        Group finalPage = new Group();
        Group ques = new Group();
        //Финальная сцена
        Text finText = new Text("Выбранный вами риэлтор свяжется с вами в ближайшее время для уточнения деталей.");
        finText.setLayoutX(20);
        finText.setLayoutY(350);
        finText.setStyle("-fx-font: 18 arial");
        Button closeScene = new Button();
        closeScene.setText("Закрыть приложение");
        closeScene.setLayoutX(300);
        closeScene.setLayoutY(400);
        closeScene.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                primaryStage.close();
            }
        });
        finalPage.getChildren().addAll(finText, closeScene);
        //Сцена с риэлторами
        VBox box = new VBox();
        Text text1 = new Text("Наши риэлторы:");
        text1.setStyle("-fx-font: 24 arial");
        box.getChildren().add(text1);
        for (int i = 0;i<5;i++){
            Group cell = new Group();
            String path = "/images/pic"+ (i+1) +".png";
            Image Image = new Image(getClass().getResource(path).toString());
            ImageView imageView = new ImageView(Image);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(90);
            imageView.setLayoutX(0);
            Text text = new Text(realtorList[i].getName()+"\nТелефон: "+realtorList[i].getPhone()+"\nРейтинг: "+realtorList[i].getRating()+"/5");
            text.setLayoutX(100);
            text.setLayoutY(20);
            text.setStyle("-fx-font: 20 arial");
            cell.getChildren().addAll(text, imageView);
            box.getChildren().add(cell);
        }
        Button btn = new Button();
        btn.setText("Главное меню");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                primaryStage.getScene().setRoot(mainMenu);
            }
        });
        btn.setLayoutX(0);
        btn.setLayoutY(750);
        realtors.getChildren().addAll(btn, box);
        //Сцена с квартирами
        VBox box1 = new VBox();
        Text text2 = new Text("Доступные для покупки квартиры:");
        text2.setStyle("-fx-font: 24 arial");
        box1.getChildren().add(text2);
        for (int i = 0;i<11;i=i+2){
            Group cell = new Group();
            Image Image = new Image(getClass().getResource(flatList[i].getPath()).toString());
            ImageView imageView = new ImageView(Image);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(120);
            imageView.setLayoutX(0);
            Text text = new Text(flatList[i].getAddress()+"\nКоличество комнат: "+flatList[i].getRooms()+"\nПлощадь: "+flatList[i].getArea()+"\nСтоимость: "+flatList[i].getPrice()+"\n");
            text.setLayoutX(130);
            text.setLayoutY(20);
            text.setStyle("-fx-font: 18 arial");
            cell.getChildren().addAll(text, imageView);
            box1.getChildren().add(cell);
        }
        Button btn3 = new Button();
        btn3.setText("Главное меню");
        btn3.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                primaryStage.getScene().setRoot(mainMenu);
            }
        });
        btn3.setLayoutX(0);
        btn3.setLayoutY(750);
        Button btnPage1 = new Button();
        btnPage1.setText("1");
        btnPage1.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                primaryStage.getScene().setRoot(flats);
            }
        });
        btnPage1.setLayoutX(710);
        btnPage1.setLayoutY(750);
        btnPage1.setPrefWidth(30);
        Button btnPage2 = new Button();
        btnPage2.setText("2");
        btnPage2.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                primaryStage.getScene().setRoot(flats2);
            }
        });
        btnPage2.setLayoutX(740);
        btnPage2.setLayoutY(750);
        btnPage2.setPrefWidth(30);
        Text pages = new Text("Страницы: ");
        pages.setLayoutX(625);
        pages.setLayoutY(770);
        pages.setStyle("-fx-font: 16 arial");
        flats.getChildren().addAll(btn3, box1, btnPage1, btnPage2, pages);
        //Сцена с квартирами 2
        VBox box3 = new VBox();
        Text text3 = new Text("Доступные для покупки квартиры:");
        text3.setStyle("-fx-font: 24 arial");
        box3.getChildren().add(text3);
        for (int i = 1;i<12;i=i+2){
            Group cell = new Group();
            Image Image = new Image(getClass().getResource(flatList[i].getPath()).toString());
            ImageView imageView = new ImageView(Image);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(120);
            imageView.setLayoutX(0);
            Text text = new Text(flatList[i].getAddress()+"\nКоличество комнат: "+flatList[i].getRooms()+"\nПлощадь: "+flatList[i].getArea()+"\nСтоимость: "+flatList[i].getPrice()+"\n");
            text.setLayoutX(130);
            text.setLayoutY(20);
            text.setStyle("-fx-font: 18 arial");
            cell.getChildren().addAll(text, imageView);
            box3.getChildren().add(cell);
        }
        Button btn4 = new Button();
        btn4.setText("Главное меню");
        btn4.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                primaryStage.getScene().setRoot(mainMenu);
            }
        });
        btn4.setLayoutX(0);
        btn4.setLayoutY(750);
        Button btnPage12 = new Button();
        btnPage12.setText("1");
        btnPage12.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                primaryStage.getScene().setRoot(flats);
            }
        });
        btnPage12.setLayoutX(710);
        btnPage12.setLayoutY(750);
        btnPage12.setPrefWidth(30);
        Button btnPage22 = new Button();
        btnPage22.setText("2");
        btnPage22.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                primaryStage.getScene().setRoot(flats2);
            }
        });
        btnPage22.setLayoutX(740);
        btnPage22.setLayoutY(750);
        btnPage22.setPrefWidth(30);
        Text pages2 = new Text("Страницы: ");
        pages2.setLayoutX(625);
        pages2.setLayoutY(770);
        pages2.setStyle("-fx-font: 16 arial");
        flats2.getChildren().addAll(btn4, box3, btnPage12, btnPage22, pages2);
        //Сцена с подбором квартиры
        Client client = new Client();
        VBox ank = new VBox();
        Text op = new Text("Заполните анкету и мы подберем вам квартиры:");
        op.setStyle("-fx-font: 22 arial");
        Group g1 = new Group();
        ObservableList<String> districs = FXCollections.observableArrayList("Московский", "Невский", "Кировский", "Адмиралтейский");
        ComboBox<String> districsComboBox = new ComboBox<String>(districs);
        districsComboBox.setOnAction(event -> client.getAppForm().setDistrict(districsComboBox.getValue()));
        districsComboBox.setValue("Московский");
        districsComboBox.setLayoutX(250);
        Text dis = new Text("Район:");
        dis.setStyle("-fx-font: 18 arial");
        dis.setLayoutY(20);
        g1.getChildren().addAll(dis, districsComboBox);
        Group g2 = new Group();
        TextField textField1 = new TextField();
        textField1.setPrefColumnCount(10);
        textField1.setLayoutX(250);
        Text ro = new Text("Количество комнат:");
        ro.setStyle("-fx-font: 18 arial");
        ro.setLayoutY(20);
        g2.getChildren().addAll(ro, textField1);
        Group g3 = new Group();
        TextField textField2 = new TextField();
        textField2.setPrefColumnCount(10);
        textField2.setLayoutX(250);
        Text ms = new Text("Минимальная площадь:");
        ms.setStyle("-fx-font: 18 arial");
        ms.setLayoutY(20);
        g3.getChildren().addAll(ms, textField2);
        Group g4 = new Group();
        TextField textField3 = new TextField();
        textField3.setPrefColumnCount(10);
        textField3.setLayoutX(250);
        Text mas = new Text("Максимальная площадь:");
        mas.setStyle("-fx-font: 18 arial");
        mas.setLayoutY(20);
        g4.getChildren().addAll(mas, textField3);
        Group g5 = new Group();
        TextField textField4 = new TextField();
        textField4.setPrefColumnCount(10);
        textField4.setLayoutX(250);
        Text mc = new Text("Минимальная стоимость:");
        mc.setStyle("-fx-font: 18 arial");
        mc.setLayoutY(20);
        g5.getChildren().addAll(mc, textField4);
        Group g6 = new Group();
        TextField textField5 = new TextField();
        textField5.setPrefColumnCount(10);
        textField5.setLayoutX(250);
        Text mac = new Text("Максимальная стоимость:");
        mac.setStyle("-fx-font: 18 arial");
        mac.setLayoutY(20);
        g6.getChildren().addAll(mac, textField5);
        Group g7 = new Group();
        TextField textField6 = new TextField();
        textField6.setPrefColumnCount(10);
        textField6.setLayoutX(250);
        Text nt = new Text("Ваше имя:");
        nt.setStyle("-fx-font: 18 arial");
        nt.setLayoutY(20);
        g7.getChildren().addAll(nt, textField6);
        Group g8 = new Group();
        TextField textField7 = new TextField();
        textField7.setPrefColumnCount(10);
        textField7.setLayoutX(250);
        Text ntt = new Text("Номер телефона:");
        ntt.setStyle("-fx-font: 18 arial");
        ntt.setLayoutY(20);
        g8.getChildren().addAll(ntt, textField7);
        Button btnTest = new Button();
        btnTest.setText("Подобрать квартиры");
        btnTest.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                try{
                    client.setName(textField6.getText());
                    client.setPhone(textField7.getText());
                    if (textField1.getText().equals("")) client.getAppForm().setRoomsNum(99);
                    else client.getAppForm().setRoomsNum(Integer.parseInt(textField1.getText()));
                    if (textField2.getText().equals("")) client.getAppForm().setMinArea(0);
                    else client.getAppForm().setMinArea(Integer.parseInt(textField2.getText()));
                    if (textField3.getText().equals("")) client.getAppForm().setMaxArea(999);
                    else client.getAppForm().setMaxArea(Integer.parseInt(textField3.getText()));
                    if (textField4.getText().equals("")) client.getAppForm().setMinPrice(0);
                    else client.getAppForm().setMinPrice(Integer.parseInt(textField4.getText()));
                    if (textField5.getText().equals("")) client.getAppForm().setMaxPrice(99999999);
                    else client.getAppForm().setMaxPrice(Integer.parseInt(textField5.getText()));
                    int flatsShow = 0;
                    VBox gflatBox = new VBox();
                    Button mmbnt = new Button();
                    mmbnt.setText("Главное меню");
                    mmbnt.setOnAction(new EventHandler<ActionEvent>() {

                        @Override
                        public void handle(ActionEvent event) {
                            primaryStage.getScene().setRoot(mainMenu);
                        }
                    });
                    mmbnt.setLayoutX(0);
                    mmbnt.setLayoutY(750);
                    goodFlats.getChildren().add(mmbnt);
                    Text wfft = new Text("Вам подойдут следующие квартиры:");
                    wfft.setStyle("-fx-font: 20 arial");
                    gflatBox.getChildren().add(wfft);
                    Text wdfft = new Text("К сожалению ни одна наша квартира вам не подходит.");
                    wdfft.setLayoutY(20);
                    wdfft.setStyle("-fx-font: 20 arial");
                    ArrayList<Integer> flats = new ArrayList<Integer>();
                    for (int i = 0;i<12;i++){
                        ApplicationForm f = client.getAppForm();
                        if (flatList[i].getFullAddress().getDistrict().equals(f.getDistrict()) && (flatList[i].getArea()>=f.getMinArea()) && (flatList[i].getArea()<=f.getMaxArea()) && (flatList[i].getPrice()>=f.getMinPrice()) && (flatList[i].getPrice()<=f.getMaxPrice()) && (f.getRoomsNum() == 99 || flatList[i].getRooms() == f.getRoomsNum())){
                        	flatsShow++;
                        	flats.add(i+1);
                            Group cell = new Group();
                            Image Image = new Image(getClass().getResource(flatList[i].getPath()).toString());
                            ImageView imageView = new ImageView(Image);
                            imageView.setPreserveRatio(true);
                            imageView.setFitWidth(120);
                            imageView.setLayoutX(0);
                            Text text = new Text(flatList[i].getAddress()+"\nКоличество комнат: "+flatList[i].getRooms()+"\nПлощадь: "+flatList[i].getArea()+"\nСтоимость: "+flatList[i].getPrice()+"\n");
                            text.setLayoutX(130);
                            text.setLayoutY(20);
                            text.setStyle("-fx-font: 18 arial");
                            Realtor[] goodRealtorsList = new Realtor[]{flatList[i].getRealtor1(), flatList[i].getRealtor2()};
                            Button nxtpbtn = new Button();
                            nxtpbtn.setText("Подобрать риэлтора");
                            nxtpbtn.setOnAction(new EventHandler<ActionEvent>() {

                                @Override
                                public void handle(ActionEvent event) {
                                    VBox grBox = new VBox();
                                    Text text1 = new Text("Наши риэлторы, которые могут помочь вам купить квартиру:");
                                    text1.setStyle("-fx-font: 22 arial");
                                    grBox.getChildren().add(text1);
                                    //Realtor[] goodRealtors = new Realtor[]{flatList[i].getRealtor1(), flatList[i].getRealtor2()};
                                    for (int i = 0;i<2;i++){
                                        Group cell = new Group();
                                        String path = goodRealtorsList[i].getImgPath();
                                        Image Image = new Image(getClass().getResource(path).toString());
                                        ImageView imageView = new ImageView(Image);
                                        imageView.setPreserveRatio(true);
                                        imageView.setFitWidth(90);
                                        imageView.setLayoutX(0);
                                        Text text = new Text(goodRealtorsList[i].getName()+"\nТелефон: "+goodRealtorsList[i].getPhone()+"\nРейтинг: "+goodRealtorsList[i].getRating()+"/5");
                                        text.setLayoutX(100);
                                        text.setLayoutY(20);
                                        text.setStyle("-fx-font: 20 arial");
                                        Button sfbtn = new Button();
                                        sfbtn.setText("Выбрать");
                                        sfbtn.setLayoutX(600);
                                        sfbtn.setLayoutY(20);
                                        sfbtn.setOnAction(new EventHandler<ActionEvent>() {

                                            @Override
                                            public void handle(ActionEvent event) {
                                                primaryStage.getScene().setRoot(finalPage);
                                            }
                                        });
                                        cell.getChildren().addAll(text, imageView, sfbtn);
                                        grBox.getChildren().add(cell);
                                    }
                                    Button btn = new Button();
                                    btn.setText("Главное меню");
                                    btn.setOnAction(new EventHandler<ActionEvent>() {

                                        @Override
                                        public void handle(ActionEvent event) {
                                            primaryStage.getScene().setRoot(mainMenu);
                                        }
                                    });
                                    btn.setLayoutX(0);
                                    btn.setLayoutY(750);
                                    goodRealtors.getChildren().addAll(btn, grBox);
                                    primaryStage.getScene().setRoot(goodRealtors);
                                }
                            });
                            nxtpbtn.setLayoutX(600);
                            nxtpbtn.setLayoutY(90);
                            cell.getChildren().addAll(text, imageView, nxtpbtn);
                            gflatBox.getChildren().add(cell);
                            }
                        }
                        if (flatsShow == 0) goodFlats.getChildren().add(wdfft);
                        else goodFlats.getChildren().addAll(gflatBox);
                    primaryStage.getScene().setRoot(goodFlats);
                    try {
                    	stmt = conn.createStatement();
                    	rset = stmt.executeQuery("SELECT * FROM clients ORDER BY id DESC");
                    	rset.next();
                    	lastClientId = Integer.parseInt(rset.getString(1)) + 1;
                    	System.out.println(lastClientId);
                    	rset.close();
                    	pstmt = conn.prepareStatement("INSERT INTO clients VALUES (?, ?, ?)");
            			pstmt.setInt(1, lastClientId);
            			pstmt.setString(2, textField6.getText());
            			pstmt.setString(3, textField7.getText());
            			pstmt.execute();
            			pstmt.close();
            			
                    	pstmt = conn.prepareStatement("INSERT INTO application_forms VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            			pstmt.setInt(1, lastClientId);
            			pstmt.setString(2, client.getAppForm().getDistrict());
            			if (client.getAppForm().getRoomsNum() == 99)
            				pstmt.setNull(3, Types.INTEGER);
            			else
            				pstmt.setInt(3, client.getAppForm().getRoomsNum());
            			if (client.getAppForm().getMinArea() == 0)
            				pstmt.setNull(4, Types.INTEGER);
            			else
            				pstmt.setInt(4, client.getAppForm().getMinArea());
            			if (client.getAppForm().getMaxArea() == 999)
            				pstmt.setNull(5, Types.INTEGER);
            			else
            				pstmt.setInt(5, client.getAppForm().getMaxArea());
            			if (client.getAppForm().getMinPrice() == 0)
            				pstmt.setNull(6, Types.INTEGER);
            			else
            				pstmt.setInt(6, client.getAppForm().getMinPrice());
            			if (client.getAppForm().getMaxPrice() == 99999999)
            				pstmt.setNull(7, Types.INTEGER);
            			else
            				pstmt.setInt(7, client.getAppForm().getMaxPrice());
            			pstmt.setInt(8, lastClientId);
            			pstmt.execute();
            			pstmt.close();
            			
                    	for (int i: flats) {
                    		pstmt = conn.prepareStatement("INSERT INTO flats_for_application_forms VALUES (?, ?)");
                    		pstmt.setInt(1, i);
                    		pstmt.setInt(2, lastClientId);
                    		pstmt.execute();
                    		pstmt.close();
                    	}
                    }
                    catch(Exception e) {
                    	System.out.println("Ошибка при заполнении таблиц");
                    }
                }
                catch(NumberFormatException e){
                    Text er1 = new Text("Имя должно содержать только буквы.");
                    er1.setStyle("-fx-font: 16 arial;-fx-fill: red");
                    er1.setLayoutX(410);
                    er1.setLayoutY(45);
                    ques.getChildren().add(er1);
                    Text er2 = new Text("Номер телефона должен состоять из цифр.");
                    er2.setStyle("-fx-font: 16 arial;-fx-fill: red");
                    er2.setLayoutX(410);
                    er2.setLayoutY(80);
                    ques.getChildren().add(er2);
                    Text er3 = new Text("Количество комнат должно быть целым числом.");
                    er3.setStyle("-fx-font: 16 arial;-fx-fill: red");
                    er3.setLayoutX(410);
                    er3.setLayoutY(140);
                    ques.getChildren().add(er3);
                    Text er4 = new Text("Мин. площадь должна быть целым числом.");
                    er4.setStyle("-fx-font: 16 arial;-fx-fill: red");
                    er4.setLayoutX(410);
                    er4.setLayoutY(175);
                    ques.getChildren().add(er4);
                    Text er5 = new Text("Макс. площадь должна быть целым числом.");
                    er5.setStyle("-fx-font: 16 arial;-fx-fill: red");
                    er5.setLayoutX(410);
                    er5.setLayoutY(205);
                    ques.getChildren().add(er5);
                    Text er6 = new Text("Мин. стоимость должна быть целым числом.");
                    er6.setStyle("-fx-font: 16 arial;-fx-fill: red");
                    er6.setLayoutX(410);
                    er6.setLayoutY(235);
                    ques.getChildren().add(er6);
                    Text er7 = new Text("Макс. стоимость должна быть целым числом.");
                    er7.setStyle("-fx-font: 16 arial;-fx-fill: red");
                    er7.setLayoutX(410);
                    er7.setLayoutY(270);
                    ques.getChildren().add(er7);
                }
            
            }
        });
        ank.setPrefHeight(500);
        ank.getChildren().addAll(op,g7, g8, g1, g2, g3, g4, g5, g6, btnTest);
        ques.getChildren().addAll(ank);
        //Сцена с подобранными квартирами
        
        //Сцена с главным меню
        Button btn1 = new Button();
        btn1.setText("Показать все квартиры");
        btn1.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                primaryStage.getScene().setRoot(flats);
            }
        });
        btn1.setLayoutX(270);
        btn1.setLayoutY(460);
        btn1.setPrefWidth(200);
        Button btn2 = new Button();
        btn2.setText("Показать всех риэлторов");
        btn2.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                primaryStage.getScene().setRoot(realtors);
            }
        });
        btn2.setLayoutX(270);
        btn2.setLayoutY(430);
        btn2.setPrefWidth(200);
        Button btnQues = new Button();
        btnQues.setText("Подобрать квартиру");
        btnQues.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                primaryStage.getScene().setRoot(ques);
            }
        });
        btnQues.setLayoutX(270);
        btnQues.setLayoutY(400);
        btnQues.setPrefWidth(200);
        Text an = new Text("АГЕНТСВО НЕДВИЖИМОСТИ");
        an.setStyle("-fx-font: 48 arial");
        an.setLayoutY(50);
        an.setLayoutX(40);
        //Group root = new Group();
        
        mainMenu.getChildren().addAll(btn1, btn2, btnQues, an);
        
        Scene scene = new Scene(mainMenu, 770, 780);
        
        
        primaryStage.setTitle("Агентсво Недвижимости");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static String windows1251_to_utf8(String str_1251){
    	try {
        String str_utf8 = "";
        for(int i=0;i<str_1251.length();i++){
            int c = Character.codePointAt(str_1251,i);
            switch (c){
                case 184: c=1105;break;
                case 168: c=1025;break;
                case 175: c=1198; break; 
                case 191: c=1199; break; 
                case 170: c=1256; break; 
                case 186: c=1257; break; 
            }
            if (256>c && c>191) c=c+848;
            switch(c) {
                case 1111: c=1199;break;   
                case 1031: c= 1198; break; 
                case 1108: c=1257; break;
                case 1028: c=1256; break;
            }
            str_utf8=str_utf8+(char)(c);
        }
        return str_utf8;}
    	catch(Exception e) {
    		return e.toString();
    	}
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}


DELETE FROM item_ja;
DELETE FROM product_ja;
DELETE FROM category_ja;

INSERT INTO account VALUES('j2ee-日本', 'yourname@yourdomain.com','The', 'Duke', 'OK', '大田1-10-1',
'', '港区', '東京都', '〒107-0000', 'Japan', '03-5555-5555')
;
INSERT INTO signon VALUES('j2ee-日本','j2ee-日本')
;
INSERT INTO profile VALUES('j2ee-日本','Japanese','fish','1','1')
;
INSERT INTO category_ja VALUES ('FISH','魚','<image src="../images/fish_icon.gif"><font size="5"color="blue"> 魚</font>');
INSERT INTO category_ja VALUES ('DOGS','犬','<image src="../images/dogs_icon.gif"><font size="5"color="blue"> 犬</font>');
INSERT INTO category_ja VALUES ('REPTILES','爬虫類','<image src="../images/reptiles_icon.gif"><font size="5"color="blue"> 爬虫類</font>');
INSERT INTO category_ja VALUES ('CATS','猫','<image src="../images/cats_icon.gif"><font size="5"color="blue"> 猫</font>');
INSERT INTO category_ja VALUES ('BIRDS','鳥','<image src="../images/birds_icon.gif"><font size="5" color="blue"> 鳥</font>');


INSERT INTO product_ja VALUES ('FI-SW-01','FISH','エンゼルフィッシュ','<image src="../images/fish1.jpg"> オーストラリア産の海水魚');
INSERT INTO product_ja VALUES ('FI-SW-02','FISH','イタチザメ','<image src="../images/fish4.gif"> オーストラリア産の海水魚');
INSERT INTO product_ja VALUES ('FI-FW-01','FISH', '鯉','<image src="../images/fish3.gif"> 日本産の淡水魚');
INSERT INTO product_ja VALUES ('FI-FW-02','FISH', '金魚','<image src="../images/fish2.gif"> 中国産の淡水魚');


INSERT INTO product_ja VALUES ('K9-BD-01','DOGS','ブルドッグ','<image src="../images/dog2.gif"> 人なつっこい性質、イギリス産');
INSERT INTO product_ja VALUES ('K9-PO-02','DOGS','プードル','<image src="../images/dog6.gif"> キュートなフランス産の犬');
INSERT INTO product_ja VALUES ('K9-DL-01','DOGS', 'ダルマシアン','<image src="../images/dog5.gif"> 消防署で大活躍');
INSERT INTO product_ja VALUES ('K9-RT-01','DOGS', 'ゴールデン=レトリーバ','<image src="../images/dog1.gif"> 飼い犬に最適');
INSERT INTO product_ja VALUES ('K9-RT-02','DOGS', 'ラブラドル=レトリーバ','<image src="../images/dog5.gif"> 猟犬に最適');
INSERT INTO product_ja VALUES ('K9-CW-01','DOGS', 'チワワ','<image src="../images/dog4.gif"> とても人なつこい');


INSERT INTO product_ja VALUES ('RP-SN-01','REPTILES','ガラガラ蛇','<image src="../images/lizard3.gif"> 番犬代わりにどうぞ');
INSERT INTO product_ja VALUES ('RP-LI-02','REPTILES','イグアナ','<image src="../images/lizard2.gif"> 人なつこい緑色のお友達');
INSERT INTO product_ja VALUES ('FL-DSH-01','CATS','マンクスネコ','<image src="../images/cat3.gif"> ねずみ退治に最適');
INSERT INTO product_ja VALUES ('FL-DLH-02','CATS','ペルシアネコ','<image src="../images/cat1.gif"> プリンセスみたいな、優しい家ネコ');
INSERT INTO product_ja VALUES ('AV-CB-01','BIRDS','ボウシインコ','<image src="../images/bird4.gif"> 最高で 75 年も長生き');
INSERT INTO product_ja VALUES ('AV-SB-02','BIRDS','フィンチ','<image src="../images/bird1.gif"> 癒し系ペット');

INSERT INTO  item_ja (itemid, productid, listprice, unitcost, supplier, status, attr1) VALUES ('EST-1','FI-SW-01',11650,10.00,1,'P','大');
INSERT INTO  item_ja (itemid, productid, listprice, unitcost, supplier, status, attr1) VALUES ('EST-2','FI-SW-01',11650,10.00,1,'P','小');
INSERT INTO  item_ja (itemid, productid, listprice, unitcost, supplier, status, attr1) VALUES ('EST-3','FI-SW-02',18350,12.00,1,'P','歯がない');
INSERT INTO  item_ja (itemid, productid, listprice, unitcost, supplier, status, attr1) VALUES ('EST-4','FI-FW-01',18350,12.00,1,'P','まだら');
INSERT INTO  item_ja (itemid, productid, listprice, unitcost, supplier, status, attr1) VALUES ('EST-5','FI-FW-01',14850,12.00,1,'P','まだらなし');
INSERT INTO  item_ja (itemid, productid, listprice, unitcost, supplier, status, attr1) VALUES ('EST-6','K9-BD-01',14950,1200,1,'P','雄 おとな');
INSERT INTO  item_ja (itemid, productid, listprice, unitcost, supplier, status, attr1) VALUES ('EST-7','K9-BD-01',18250,1200,1,'P','雌 子犬');
INSERT INTO  item_ja (itemid, productid, listprice, unitcost, supplier, status, attr1) VALUES ('EST-8','K9-PO-02',18150,1200,1,'P','雄 子犬');
INSERT INTO  item_ja (itemid, productid, listprice, unitcost, supplier, status, attr1) VALUES ('EST-9','K9-DL-01',10850,1200,1,'P','まだらなし 雄 子犬');
INSERT INTO  item_ja (itemid, productid, listprice, unitcost, supplier, status, attr1) VALUES ('EST-10','K9-DL-01',10850,1200,1,'P','まだら おとな 雌');
INSERT INTO  item_ja (itemid, productid, listprice, unitcost, supplier, status, attr1) VALUES ('EST-11','RP-SN-01',2050,1200,1,'P','毒なし');
INSERT INTO  item_ja (itemid, productid, listprice, unitcost, supplier, status, attr1) VALUES ('EST-12','RP-SN-01',1850,1200,1,'P','音なし');
INSERT INTO  item_ja (itemid, productid, listprice, unitcost, supplier, status, attr1) VALUES ('EST-13','RP-LI-02',1950,1200,1,'P','緑 おとな');
INSERT INTO  item_ja (itemid, productid, listprice, unitcost, supplier, status, attr1) VALUES ('EST-14','FL-DSH-01',5850,1200,1,'P','しっぽなし');
INSERT INTO  item_ja (itemid, productid, listprice, unitcost, supplier, status, attr1) VALUES ('EST-15','FL-DSH-01',2350,1200,1,'P','しっぽあり');
INSERT INTO  item_ja (itemid, productid, listprice, unitcost, supplier, status, attr1) VALUES ('EST-16','FL-DLH-02',9350,1200,1,'P','おとな 雌');
INSERT INTO  item_ja (itemid, productid, listprice, unitcost, supplier, status, attr1) VALUES ('EST-17','FL-DLH-02',9350,1200,1,'P','おとな 雄');
INSERT INTO  item_ja (itemid, productid, listprice, unitcost, supplier, status, attr1) VALUES ('EST-18','AV-CB-01',19350,9200,1,'P','おとな 雄');
INSERT INTO  item_ja (itemid, productid, listprice, unitcost, supplier, status, attr1) VALUES ('EST-19','AV-SB-02',14550, 200,1,'P','おとな 雄');
INSERT INTO  item_ja (itemid, productid, listprice, unitcost, supplier, status, attr1) VALUES ('EST-20','FI-FW-02',550, 200,1,'P','おとな 雄');
INSERT INTO  item_ja (itemid, productid, listprice, unitcost, supplier, status, attr1) VALUES ('EST-21','FI-FW-02',529, 100,1,'P','おとな 雌');
INSERT INTO  item_ja (itemid, productid, listprice, unitcost, supplier, status, attr1) VALUES ('EST-22','K9-RT-02',13550, 10000,1,'P','おとな 雄');
INSERT INTO  item_ja (itemid, productid, listprice, unitcost, supplier, status, attr1) VALUES ('EST-23','K9-RT-02',14549, 10000,1,'P','おとな 雌');
INSERT INTO  item_ja (itemid, productid, listprice, unitcost, supplier, status, attr1) VALUES ('EST-24','K9-RT-02',25550, 9200,1,'P','おとな 雄');
INSERT INTO  item_ja (itemid, productid, listprice, unitcost, supplier, status, attr1) VALUES ('EST-25','K9-RT-02',32529, 9000,1,'P','おとな 雌');
INSERT INTO  item_ja (itemid, productid, listprice, unitcost, supplier, status, attr1) VALUES ('EST-26','K9-CW-01',12550, 9200,1,'P','おとな 雄');
INSERT INTO  item_ja (itemid, productid, listprice, unitcost, supplier, status, attr1) VALUES ('EST-27','K9-CW-01',15529, 9000,1,'P','おとな 雌');
INSERT INTO  item_ja (itemid, productid, listprice, unitcost, supplier, status, attr1) VALUES ('EST-28','K9-RT-01',15529, 9000,1,'P','おとな 雌');

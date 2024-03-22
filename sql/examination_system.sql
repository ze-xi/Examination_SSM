/*
 Navicat Premium Data Transfer

 Source Server         : llx
 Source Server Type    : MySQL
 Source Server Version : 100503 (10.5.3-MariaDB)
 Source Host           : localhost:6688
 Source Schema         : examination_system

 Target Server Type    : MySQL
 Target Server Version : 100503 (10.5.3-MariaDB)
 File Encoding         : 65001

 Date: 22/03/2024 21:45:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for college
-- ----------------------------
DROP TABLE IF EXISTS `college`;
CREATE TABLE `college`  (
  `collegeID` int NOT NULL COMMENT '课程id',
  `collegeName` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '学院名',
  PRIMARY KEY (`collegeID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of college
-- ----------------------------
INSERT INTO `college` VALUES (1, '计算机系');
INSERT INTO `college` VALUES (2, '设计系');
INSERT INTO `college` VALUES (3, '财经系');
INSERT INTO `college` VALUES (4, '音乐系');
INSERT INTO `college` VALUES (5, '影视系');
INSERT INTO `college` VALUES (6, '机械系');
INSERT INTO `college` VALUES (7, '皮影戏');
INSERT INTO `college` VALUES (8, '医学系');
INSERT INTO `college` VALUES (9, '体育系');

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course`  (
  `courseID` int NOT NULL,
  `courseName` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '课程名称',
  `teacherID` int NOT NULL,
  `courseTime` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开课时间',
  `classRoom` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开课地点',
  `courseWeek` int NULL DEFAULT NULL COMMENT '学时',
  `courseType` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '课程类型',
  `collegeID` int NOT NULL COMMENT '所属院系',
  `score` int NOT NULL COMMENT '学分',
  PRIMARY KEY (`courseID`) USING BTREE,
  INDEX `collegeID`(`collegeID` ASC) USING BTREE,
  INDEX `teacherID`(`teacherID` ASC) USING BTREE,
  CONSTRAINT `course_ibfk_1` FOREIGN KEY (`collegeID`) REFERENCES `college` (`collegeID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `course_ibfk_2` FOREIGN KEY (`teacherID`) REFERENCES `teacher` (`userID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES (1, 'C语言程序设计', 1001, '周二1-2节', '科401', 18, '必修课', 1, 3);
INSERT INTO `course` VALUES (2, 'Python爬虫技巧', 1001, '周四1-2节', 'X402', 18, '必修课', 1, 3);
INSERT INTO `course` VALUES (3, '数据结构', 1001, '周四3-4节', '科401', 18, '必修课', 1, 2);
INSERT INTO `course` VALUES (4, 'Java程序设计', 1001, '周五7-8节', '科401', 18, '必修课', 1, 2);
INSERT INTO `course` VALUES (5, '英语', 1002, '周四5-6节', 'X302', 18, '必修课', 1, 2);
INSERT INTO `course` VALUES (6, '服装设计', 1003, '周一', '科401', 18, '选修课', 2, 2);
INSERT INTO `course` VALUES (7, '医疗入门', 1001, '周二3-4', '科314', 8, '必修课', 1, 2);
INSERT INTO `course` VALUES (8, '制作一个32位操作系统复活爱人', 1008, '每天', '爱因斯坦家里', 66, '必修课', 6, 999);

-- ----------------------------
-- Table structure for resource
-- ----------------------------
DROP TABLE IF EXISTS `resource`;
CREATE TABLE `resource`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `is_teacher` tinyint NOT NULL COMMENT '是否为教师，1为老师，0为学生',
  `user_id` int NOT NULL COMMENT '教师或学生的id',
  `course_id` int NOT NULL COMMENT '课程id',
  `photos` char(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '图片的前缀和后缀，111.jpg',
  `gmt_create` date NULL DEFAULT NULL COMMENT '数据创建时间 yyyy-mm-dd',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 80 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of resource
-- ----------------------------
INSERT INTO `resource` VALUES (62, 1, 1001, 1, 'f4c935a4-bb97-4ded-93bf-ccd7aecba06b.zip', '2024-02-08');
INSERT INTO `resource` VALUES (63, 1, 1001, 1, '97c56238-90d8-48ec-a1c4-6345be8dbc6a.zip', '2024-03-05');
INSERT INTO `resource` VALUES (64, 1, 1001, 1, '05216945-bd50-419b-ab1d-d5f15fa24791.zip', '2024-03-05');
INSERT INTO `resource` VALUES (65, 1, 1001, 1, 'aa1ada71-87c4-4148-92e8-1c9f98e2ebf1.zip', '2024-03-05');
INSERT INTO `resource` VALUES (66, 1, 1001, 1, '5054f310-56d3-46a8-980a-8e502e70318c.zip', '2024-03-05');
INSERT INTO `resource` VALUES (67, 1, 1001, 1, '4a8eb6f0-fbb2-4e8c-aa14-9cf255d0cabd.zip', '2024-03-05');
INSERT INTO `resource` VALUES (68, 1, 1001, 1, '4953b6f5-0880-47fb-95ba-b146d10ead63.zip', '2024-03-05');
INSERT INTO `resource` VALUES (69, 1, 1001, 1, '080442c6-f8c4-4349-92d3-dd298a568024.zip', '2024-03-05');
INSERT INTO `resource` VALUES (70, 1, 1001, 1, 'c0782b2c-0405-48f3-9378-106315def2f9.zip', '2024-03-05');
INSERT INTO `resource` VALUES (71, 1, 1001, 1, 'e488b7b0-9663-4623-96c9-45bc10088ac0.zip', '2024-03-05');
INSERT INTO `resource` VALUES (72, 1, 1001, 1, '9f8069d9-c451-4d9f-bac4-c977a2a5dc99.zip', '2024-03-05');
INSERT INTO `resource` VALUES (73, 1, 1001, 1, '70a7151d-bdaf-4708-9957-dd48988d81e5.jpg', '2024-03-05');
INSERT INTO `resource` VALUES (74, 0, 10001, 5, '1ec69aa4-7a96-4b2a-8437-5ce48a0fddac.zip', '2024-03-05');
INSERT INTO `resource` VALUES (75, 0, 10001, 5, '3a6bf0bd-9d4f-4abe-99df-4949656575d2.zip', '2024-03-05');
INSERT INTO `resource` VALUES (77, 1, 1002, 5, '64016fdc-e54f-42cc-be14-7bd818ed07b7.png', '2024-03-05');
INSERT INTO `resource` VALUES (78, 1, 1002, 5, 'ab3477cc-9f94-4e42-b6d4-8e24924f6fc4.zip', '2024-03-05');
INSERT INTO `resource` VALUES (79, 1, 1002, 5, '59bc62c6-9231-4a7e-b469-a2bb15012208.zip', '2024-03-05');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `roleID` int NOT NULL,
  `roleName` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `permissions` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限',
  PRIMARY KEY (`roleID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (0, 'admin', NULL);
INSERT INTO `role` VALUES (1, 'teacher', NULL);
INSERT INTO `role` VALUES (2, 'student', NULL);

-- ----------------------------
-- Table structure for selectedcourse
-- ----------------------------
DROP TABLE IF EXISTS `selectedcourse`;
CREATE TABLE `selectedcourse`  (
  `courseID` int NOT NULL,
  `studentID` int NOT NULL,
  `mark` int NULL DEFAULT NULL COMMENT '成绩',
  INDEX `courseID`(`courseID` ASC) USING BTREE,
  INDEX `studentID`(`studentID` ASC) USING BTREE,
  CONSTRAINT `selectedcourse_ibfk_1` FOREIGN KEY (`courseID`) REFERENCES `course` (`courseID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `selectedcourse_ibfk_2` FOREIGN KEY (`studentID`) REFERENCES `student` (`userID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of selectedcourse
-- ----------------------------
INSERT INTO `selectedcourse` VALUES (2, 10001, 12);
INSERT INTO `selectedcourse` VALUES (1, 10001, 80);
INSERT INTO `selectedcourse` VALUES (1, 10002, 66);
INSERT INTO `selectedcourse` VALUES (1, 10003, NULL);
INSERT INTO `selectedcourse` VALUES (2, 10003, 99);
INSERT INTO `selectedcourse` VALUES (5, 10001, NULL);
INSERT INTO `selectedcourse` VALUES (3, 10001, NULL);
INSERT INTO `selectedcourse` VALUES (7, 10001, 80);

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `userID` int NOT NULL AUTO_INCREMENT,
  `userName` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '姓名(非用户名)',
  `sex` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '性别',
  `birthYear` date NULL DEFAULT NULL COMMENT '出生日期',
  `grade` date NULL DEFAULT NULL COMMENT '入学时间',
  `collegeID` int NOT NULL COMMENT '院系id',
  PRIMARY KEY (`userID`) USING BTREE,
  INDEX `collegeID`(`collegeID` ASC) USING BTREE,
  CONSTRAINT `student_ibfk_1` FOREIGN KEY (`collegeID`) REFERENCES `college` (`collegeID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 10007 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES (10001, '小黄', '男', '1996-09-02', '2015-09-02', 1);
INSERT INTO `student` VALUES (10002, '小米', '女', '1995-09-14', '2015-09-02', 3);
INSERT INTO `student` VALUES (10003, '小陈', '女', '1996-09-02', '2015-09-02', 2);
INSERT INTO `student` VALUES (10004, '小华', '男', '1996-09-02', '2015-09-02', 2);
INSERT INTO `student` VALUES (10005, '小左', '女', '1996-09-02', '2015-09-02', 2);
INSERT INTO `student` VALUES (10006, '小拉', '女', '1996-09-02', '2015-09-02', 1);

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher`  (
  `userID` int NOT NULL AUTO_INCREMENT,
  `userName` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '姓名',
  `sex` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '性别',
  `birthYear` date NOT NULL COMMENT '出生日期',
  `degree` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '学历',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职称',
  `grade` date NULL DEFAULT NULL COMMENT '入职时间',
  `collegeID` int NOT NULL COMMENT '院系',
  PRIMARY KEY (`userID`) USING BTREE,
  INDEX `collegeID`(`collegeID` ASC) USING BTREE,
  CONSTRAINT `teacher_ibfk_1` FOREIGN KEY (`collegeID`) REFERENCES `college` (`collegeID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1011 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of teacher
-- ----------------------------
INSERT INTO `teacher` VALUES (1001, '刘老师', '女', '1990-03-08', '硕士', '副教授', '2015-09-02', 2);
INSERT INTO `teacher` VALUES (1002, '张嘉文老师', '男', '1996-09-02', '本科', '普通教师', '2015-09-02', 1);
INSERT INTO `teacher` VALUES (1003, '软老师', '男', '1996-09-02', '硕士', '助教', '2017-07-07', 1);
INSERT INTO `teacher` VALUES (1004, '邓老师', '男', '1996-09-08', '本科', '副教授', '2019-09-08', 1);
INSERT INTO `teacher` VALUES (1005, '罗教授', '男', '1992-06-03', '博士', '教授', '2023-11-16', 2);
INSERT INTO `teacher` VALUES (1006, '秋实老师', '男', '2003-10-10', '本科', '普通教师', '2024-01-25', 3);
INSERT INTO `teacher` VALUES (1007, '张杰', '男', '2007-07-07', '本科', '讲师', '2017-07-17', 4);
INSERT INTO `teacher` VALUES (1008, '强尼银手', '男', '1997-07-17', '本科', '助教', '2077-07-07', 1);
INSERT INTO `teacher` VALUES (1009, 'pdd', '男', '1996-09-02', '高中', '讲师', '2015-09-02', 7);
INSERT INTO `teacher` VALUES (1010, '吴老师', '男', '1992-12-03', '本科', '普通教师', '2017-04-09', 5);

-- ----------------------------
-- Table structure for userlogin
-- ----------------------------
DROP TABLE IF EXISTS `userlogin`;
CREATE TABLE `userlogin`  (
  `userID` int NOT NULL AUTO_INCREMENT,
  `userName` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `role` int NOT NULL DEFAULT 2 COMMENT '角色权限',
  PRIMARY KEY (`userID`) USING BTREE,
  INDEX `role`(`role` ASC) USING BTREE,
  CONSTRAINT `userlogin_ibfk_1` FOREIGN KEY (`role`) REFERENCES `role` (`roleID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of userlogin
-- ----------------------------
INSERT INTO `userlogin` VALUES (1, 'admin', '123', 0);
INSERT INTO `userlogin` VALUES (8, '10001', '123', 2);
INSERT INTO `userlogin` VALUES (9, '10002', '123', 2);
INSERT INTO `userlogin` VALUES (10, '10003', '123', 2);
INSERT INTO `userlogin` VALUES (11, '10005', '123', 2);
INSERT INTO `userlogin` VALUES (12, '10004', '123', 2);
INSERT INTO `userlogin` VALUES (13, '10006', '123', 2);
INSERT INTO `userlogin` VALUES (14, '1001', '123', 1);
INSERT INTO `userlogin` VALUES (15, '1002', '123', 1);
INSERT INTO `userlogin` VALUES (16, '1003', '123', 1);
INSERT INTO `userlogin` VALUES (17, '1004', '123', 1);
INSERT INTO `userlogin` VALUES (18, '1005', '123', 1);
INSERT INTO `userlogin` VALUES (19, '1006', '123', 1);
INSERT INTO `userlogin` VALUES (20, '1008', '123', 1);
INSERT INTO `userlogin` VALUES (21, '1007', '123', 1);
INSERT INTO `userlogin` VALUES (22, '1009', '123', 1);
INSERT INTO `userlogin` VALUES (23, '1010', '123', 1);

SET FOREIGN_KEY_CHECKS = 1;

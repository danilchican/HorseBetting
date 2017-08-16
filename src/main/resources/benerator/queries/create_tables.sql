-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema horsebetting
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `horsebetting` ;

-- -----------------------------------------------------
-- Schema horsebetting
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `horsebetting` DEFAULT CHARACTER SET utf8 ;
USE `horsebetting` ;

-- -----------------------------------------------------
-- Table `horsebetting`.`roles`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `horsebetting`.`roles` ;

CREATE TABLE IF NOT EXISTS `horsebetting`.`roles` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `horsebetting`.`users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `horsebetting`.`users` ;

CREATE TABLE IF NOT EXISTS `horsebetting`.`users` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `role_id` INT(10) UNSIGNED NOT NULL,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  `email` VARCHAR(255) NOT NULL,
  `password` VARCHAR(60) NOT NULL,
  `balance` DECIMAL(16,2) NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `fk_role_id_idx` (`role_id` ASC),
  CONSTRAINT `users_role_id_foreign`
    FOREIGN KEY (`role_id`)
    REFERENCES `horsebetting`.`roles` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `horsebetting`.`suits`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `horsebetting`.`suits` ;

CREATE TABLE IF NOT EXISTS `horsebetting`.`suits` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(80) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `horsebetting`.`horses`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `horsebetting`.`horses` ;

CREATE TABLE IF NOT EXISTS `horsebetting`.`horses` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `suit_id` INT(10) UNSIGNED NOT NULL,
  `name` VARCHAR(80) NOT NULL,
  `age` TINYINT(3) UNSIGNED NOT NULL,
  `gender` TINYINT(1) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `fk_suit_id_idx` (`suit_id` ASC),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC),
  CONSTRAINT `horses_suit_id_foreign`
    FOREIGN KEY (`suit_id`)
    REFERENCES `horsebetting`.`suits` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `horsebetting`.`races`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `horsebetting`.`races` ;

CREATE TABLE IF NOT EXISTS `horsebetting`.`races` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(80) NOT NULL,
  `place` VARCHAR(80) NOT NULL,
  `min_rate` DECIMAL(16,2) UNSIGNED NOT NULL,
  `track_length` INT(10) UNSIGNED NOT NULL,
  `is_finished` TINYINT(1) UNSIGNED NOT NULL,
  `bet_end_date` DATETIME NOT NULL COMMENT 'End date receiving bettings.',
  `started_at` DATETIME NOT NULL COMMENT 'Start date of race.',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `horsebetting`.`participants`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `horsebetting`.`participants` ;

CREATE TABLE IF NOT EXISTS `horsebetting`.`participants` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `horse_id` INT(10) UNSIGNED NOT NULL,
  `race_id` INT(10) UNSIGNED NOT NULL,
  `coefficient` DECIMAL(4,2) UNSIGNED NOT NULL,
  `is_winner` TINYINT(1) UNSIGNED NULL DEFAULT NULL,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `fk_horse_id_idx` (`horse_id` ASC),
  INDEX `fk_race_id_idx` (`race_id` ASC),
  PRIMARY KEY (`id`),
  UNIQUE INDEX `horse_race_id_UNIQUE` (`horse_id` ASC, `race_id` ASC),
  CONSTRAINT `horse_race_horse_id_foreign`
    FOREIGN KEY (`horse_id`)
    REFERENCES `horsebetting`.`horses` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `horse_race_race_id_foreign`
    FOREIGN KEY (`race_id`)
    REFERENCES `horsebetting`.`races` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `horsebetting`.`bets`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `horsebetting`.`bets` ;

CREATE TABLE IF NOT EXISTS `horsebetting`.`bets` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` INT(10) UNSIGNED NOT NULL,
  `participant_id` INT(10) UNSIGNED NOT NULL,
  `amount` DECIMAL(16,2) UNSIGNED NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `fk_user_id_idx` (`user_id` ASC),
  INDEX `bets_participant_id_foreign_idx` (`participant_id` ASC),
  CONSTRAINT `bets_user_id_foreign`
    FOREIGN KEY (`user_id`)
    REFERENCES `horsebetting`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `bets_participant_id_foreign`
    FOREIGN KEY (`participant_id`)
    REFERENCES `horsebetting`.`participants` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `horsebetting`.`news`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `horsebetting`.`news` ;

CREATE TABLE IF NOT EXISTS `horsebetting`.`news` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` INT(10) UNSIGNED NOT NULL,
  `title` VARCHAR(255) NOT NULL,
  `slug` VARCHAR(45) NOT NULL,
  `content` TEXT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `slug_UNIQUE` (`slug` ASC),
  INDEX `news_user_id_foreign_idx` (`user_id` ASC),
  CONSTRAINT `news_user_id_foreign`
    FOREIGN KEY (`user_id`)
    REFERENCES `horsebetting`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `horsebetting`.`password_resets`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `horsebetting`.`password_resets` ;

CREATE TABLE IF NOT EXISTS `horsebetting`.`password_resets` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(255) NOT NULL,
  `token` VARCHAR(255) NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX `password_resets_token_idx` (`token` ASC),
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `token_UNIQUE` (`token` ASC))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

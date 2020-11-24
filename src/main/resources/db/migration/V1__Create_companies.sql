CREATE TABLE companies(
  ID SERIAL PRIMARY KEY NOT NULL,
  NAME       VARCHAR(255),
  BRANCHE TEXT,
  WEBSITE TEXT ,
  EMAIL TEXT
);
create table location (
        ID  Serial primary key not null,
        CITY TEXT,
        NUMMER TEXT,
        PLZ TEXT,
        STREET TEXT,
        COMPANIES_ID INT

    );

alter table location
       add constraint FK29w2secbceb7nh4jxco4mhscp
       foreign key (COMPANIES_ID)
       references companies(ID)
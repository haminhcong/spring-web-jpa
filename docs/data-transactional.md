# SQL Transaction Isolation and Locking notes

## Problems:

A multiuser database must provide the following:

- The assurance that users can access data at the same time (data concurrency)
- The assurance that each user sees a consistent view of the data (data consistency), including visible 
changes made by the user's own transactions and committed transactions of other users 


### SQL Transaction Isolation Levels

- Ref: https://www.oracle.com/technetwork/testcontent/o65asktom-082389.html
- Oracle-Database-Transactions-and-Locking-Revealed
- Thomas Kyte, Darl Kuhn-Expert Oracle Database Architecture-Apress

The ANSI/ISO SQL standard defines four levels of transaction isolation, with different possible outcomes for the same transaction scenario. That is, the same work performed in the same fashion with the same inputs may result in different answers, depending on your isolation level. These levels are defined in terms of three phenomena that are either permitted or not at a given isolation level: 

- Dirty read: The meaning of this term is as bad as it sounds. You're permitted to read uncommitted, or dirty , data. You can achieve this effect by just opening an OS file that someone else is writing and reading whatever data happens to be there. Data integrity is compromised, foreign keys are violated, and unique constraints are ignored.
- Nonrepeatable read: This simply means that if you read a row at time T1 and try to reread that row at time T2, the row may have changed. It may have disappeared, it may have been updated, and so on.
- Phantom read: This means that if you execute a query at time T1 and re-execute it at time T2, additional rows may have been added to the database, which may affect your results. This differs from a nonrepeatable read in that with a phantom read, data you already read hasn't been changed, but instead, more data satisfies your query criteria than before. 

Note that the ANSI/ISO SQL standard defines transaction-level characteristics, not just individual statement-by-statement-level characteristics. I'll examine transaction-level isolation, not just statement-level isolation.

The SQL isolation levels are defined based on whether they allow each of the preceding phenomena. It's interesting to note that the SQL standard doesn't impose a specific locking scheme or mandate particular behaviors, but rather describes these isolation levels in terms of these phenomena—allowing for many different locking/concurrency mechanisms to exist (see Table 1).


- Isolation Level 	Dirty Read 	Nonrepeatable Read 	Phantom Read
- READ UNCOMMITTED 	Permitted 	Permitted 	          Permitted
- READ COMMITTED 	  -- 	        Permitted 	          Permitted
- REPEATABLE READ 	-- 	        -- 	                  Permitted
- SERIALIZABLE    	-- 	        -- 	                  --


Table 1: ANSI isolation levels 

### Oracle Isolation Levels

Oracle provides these transaction isolation levels:

- Read committed 	This is the default transaction isolation level. Each query executed by a transaction sees only data that was committed before the query (not the transaction) began. An Oracle query never reads dirty (uncommitted) data.Because Oracle does not prevent other transactions from modifying the data read by a query, that data can be changed by other transactions between two executions of the query. Thus, a transaction that runs a given query twice can experience both nonrepeatable read and phantoms.
- Serializable 	Serializable transactions see only those changes that were committed at the time the transaction began, plus those changes made by the transaction itself through INSERT, UPDATE, and DELETE statements. Serializable transactions do not experience nonrepeatable reads or phantoms.
- Read-only 	Read-only transactions see only those changes that were committed at the time the transaction began and do not allow INSERT, UPDATE, and DELETE statements.


## Blog, References



## Sample projects

- https://github.com/spring-projects/spring-data-examples


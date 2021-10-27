from sqlite3.dbapi2 import IntegrityError
import sqlite3


#Database creation and connection
def init_db(database, schema):
    connection = sqlite3.connect(database)

    with open(schema) as f:
        connection.executescript(f.read())

    connection.commit()
    connection.close()

def get_db_connection(database):

    connection = sqlite3.connect(database)
    connection.row_factory = sqlite3.Row
    return connection

#ACCOUNT MANAGE
#insert account (gestisce anche se gia esiste)
def insertAccount(database, userid, tokenid, email):
    connection = get_db_connection(database)
    cur = connection.cursor()

    try:
        cur.execute("INSERT INTO accounts (userID, tokenID, email) VALUES (?,?,?)", (userid, tokenid, email, ))
    except IntegrityError:
        # Account registered yet: update tokenID
        cur.execute("DELETE FROM accounts WHERE userID='"+userid+"';")
        cur.execute("INSERT INTO accounts (userID, tokenID, email) VALUES (?,?,?)", (userid, tokenid, email, ))
        pass

    connection.commit()
    connection.close()

#check if exist an account with passed tokenID (ret 0/1)
def checkIfAccountExists(database, tokenid):
    connection = get_db_connection(database)

    accountInDatabase = connection.execute('SELECT EXISTS(SELECT tokenID FROM accounts WHERE tokenID="'+tokenid+'");').fetchall()
    accountInDatabase = dict(accountInDatabase[0])['EXISTS(SELECT tokenID FROM accounts WHERE tokenID="'+tokenid+'")']

    connection.commit()
    connection.close()

    return accountInDatabase

#check if exist an account with passed email (ret 0/1)
def checkIfEmailExists(database, email):
    connection = get_db_connection(database)

    emailInDatabase = connection.execute('SELECT EXISTS(SELECT email FROM accounts WHERE email="'+email+'");').fetchall()
    emailInDatabase = dict(emailInDatabase[0])['EXISTS(SELECT email FROM accounts WHERE email="'+email+'")']
    connection.commit()
    connection.close()

    return emailInDatabase

#check if exist an account with passed userid (ret 0/1)
def checkIfUserIDExists(database, userid):
    connection = get_db_connection(database)

    accountInDatabase = connection.execute('SELECT EXISTS(SELECT userID FROM accounts WHERE userID="'+userid+'");').fetchall()
    accountInDatabase = dict(accountInDatabase[0])['EXISTS(SELECT userID FROM accounts WHERE userID="'+userid+'")']

    connection.commit()
    connection.close()

    return accountInDatabase

#delate an existing account
def deleteAccount(database, userid):
    connection = get_db_connection(database)

    resourceDeleted = False
    if checkIfUserIDExists(database,userid) == 1:
        cur = connection.cursor()
        cur.execute("DELETE FROM accounts WHERE userID='"+userid+"';")
        numDeletedAccounts = cur.execute("SELECT changes();").fetchall()
        numDeletedAccounts = dict(numDeletedAccounts[0])['changes()']
        if numDeletedAccounts > 0:
            cur.execute("DELETE FROM favorites WHERE account='"+userid+"';")
            resourceDeleted = True

    connection.commit()
    connection.close()

    return resourceDeleted

#FAVORITE MANAGE
#insert favorite news
def insertFavorite(database, userid, news):
    connection = get_db_connection(database)

    # Insert news
    resourceCreated = False
    cur = connection.cursor()
    try:
        cur.execute("INSERT INTO news (id, webTitle, webImage, webDesc, webUrl) VALUES (?,?,?,?,?)", (news['id'], news['webTitle'], news['webImage'], news['webDesc'], news['webUrl']))
    except IntegrityError:
        # News registered yet
        pass
    finally:
        try:
            cur.execute("INSERT INTO favorites (account, content) VALUES (?,?)", (userid, news['id'], ))
            resourceCreated = True
        except IntegrityError:
            # Favorite registered yet
            pass

    connection.commit()
    connection.close()

    return resourceCreated

#delete favorite news
def deleteFavorite(database, userid, news):
    connection = get_db_connection(database)

    # Delete favorite
    resourceDeleted = False
    cur = connection.cursor()

    cur.execute("DELETE FROM favorites WHERE account='"+userid+"' and content='"+news+"';")
    numDeletedFavorites = cur.execute("SELECT changes();").fetchall()
    numDeletedFavorites = dict(numDeletedFavorites[0])['changes()']
    if numDeletedFavorites > 0:
        resourceDeleted = True

    connection.commit()
    connection.close()

    return resourceDeleted

# Web server

def checkTokenID(tokenID):
    try:
        # Specify the CLIENT_ID in credentials.py of the app that accesses the backend
        idinfo = id_token.verify_oauth2_token(tokenID, requests.Request(), credentials.CLIENT_ID)
        # ID token is valid. Get the user's Google Account ID from the decoded token
        userid = idinfo['sub']
        email = idinfo['email']
        return userid, email
    except ValueError:
        # Invalid token
        pass
    return "", ""














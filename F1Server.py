
from flask import Flask
from flask_restful import Api, Resource, reqparse, request
import urllib.request, json, xmltodict
import F1ServerFunctions
import dbFunctions

app = Flask(__name__)
api = Api(app)


PATH = '/'


#Giorgia
#DATABASE = '/Users/giorg/Documents/GitHub/F1AppMobile/database.db'
#SCHEMA = '/Users/giorg/Documents/GitHub/F1AppMobile/db.sql'
#HOST = '192.168.1.225'

#Giulia
DATABASE = '/home/giulia/Desktop/Mobile/Proj/F1AppMobile/database.db'
SCHEMA = '/home/giulia/Desktop/Mobile/Proj/F1AppMobile/db.sql'
HOST = '192.168.1.139'

#Giacomo
#DATABASE = '/Users/giacomo/Desktop/progetto/F1AppMobile/database.db'
#SCHEMA = '/Users/giacomo/Desktop/progetto/F1AppMobile/db.sql'
#HOST = '192.168.1.51'


#IL DB SI RESETTA OGNI VOLTA ---> DA SISTEMARE POI

class F1Server(Resource):

    #ritorna elenco dei circuiti id,Nome,Nazione
    @app.route(PATH+"circuits", methods=['GET'])
    def get_circuits():
        url = "http://ergast.com/api/f1/current/circuits"
        response = urllib.request.urlopen(url)
        my_xml = response.read()
        my_dic = xmltodict.parse(my_xml)
        circuit_list = my_dic['MRData']["CircuitTable"]['Circuit']

        ret = []
        circuit = ""
        for c in circuit_list:
            circuitId = c['@circuitId']
            circuit = c['CircuitName'] + ',' + c['Location']['Country']
            ret.append({'id': circuitId, "circuit":circuit})

        return {"list": ret}, 200
   
    #ritorna risultati ultimi 5 anni  e pole 
    #query /circuit?name=nomedato
    @app.route(PATH+"circuit", methods=['GET'])
    def get_circuit():

        circuitName = request.args.get('name')
        results = F1ServerFunctions.get_circuit_results(circuitName)
        poles = F1ServerFunctions.get_circuit_poles(circuitName)
        ret = {'results': results, 'poles':poles}

        return ret

    #ritorna elenco teams dal 2017 id,nome NB CI METTE UN BOTTO TIPO 6 SEC
    @app.route(PATH+"teams", methods=['GET'])
    def get_teams():
        ret = []
        for n in range(2020,2022): 
            url = "http://ergast.com/api/f1/" + str(n) + "/constructors"
            response = urllib.request.urlopen(url)
            my_xml = response.read()

            my_dic = xmltodict.parse(my_xml)
            teams_list = my_dic['MRData']['ConstructorTable']['Constructor']
        
            for t in teams_list:
                teamId = t['@constructorId']
                teamName = t['Name']
                team = {'teamId': teamId, 'teamName': teamName}
                if team not in ret:
                    ret.append(team)

        return {"list": ret}, 200
    
    #ritorna informazioni di un dato team
    #query /team?name=nomedato
    @app.route(PATH+"team", methods=['GET'])
    def get_team():
        teamName = request.args.get('name').lower()

        nationality = F1ServerFunctions.get_team_nation(teamName)
        currentDrivers = F1ServerFunctions.get_team_current_drivers(teamName)
        standInfo = F1ServerFunctions.get_team_standInfo(teamName)
        totalRaceWin = F1ServerFunctions.get_team_results(teamName)
        totalChamWin = F1ServerFunctions.get_team_standings(teamName)

        ret = {'nationality': nationality, 'currentDrivers' : currentDrivers, 'standInfo':standInfo, 'totalWinsRace': totalRaceWin, 'totalChampRace': totalChamWin}

        return ret

    #ritorna elenco piloti dal 2017 id,nome NB CI METTE UN BOTTO TIPO 6 SEC
    @app.route(PATH+"drivers", methods=['GET'])
    def get_drivers():
        ret = []
        for n in range(2020,2022): 
            url = "http://ergast.com/api/f1/" + str(n) + "/drivers"
            response = urllib.request.urlopen(url)
            my_xml = response.read()

            my_dic = xmltodict.parse(my_xml)
            drivers_list = my_dic['MRData']['DriverTable']['Driver']
        
            for t in drivers_list:
                driverId = t['@driverId']
                driverName = t['GivenName']
                driverSurname = t['FamilyName']
                driver = {'driverId': driverId, 'driverName': driverName, 'driverSurname': driverSurname}
                if driver not in ret:
                    ret.append(driver)
            
        return {"list": ret}, 200

    #ritorna informazioni di un dato driver
    #query /driver?name=nomedato
    @app.route(PATH+"driver", methods=['GET'])
    def get_driver():
        driverName = request.args.get('name').lower()

        driverInfo = F1ServerFunctions.get_driver_info(driverName)
        wonraces = F1ServerFunctions.get_driver_wins(driverName)
        wonchamp =  F1ServerFunctions.get_driver_champions(driverName)
        poleraces = F1ServerFunctions.get_driver_pole(driverName)
        currentStanding = F1ServerFunctions.get_driver_currentStanding(driverName)
        teams = F1ServerFunctions.get_driver_teams(driverName)
        ret = {'driverInfo': driverInfo, 'wonRaces':wonraces, 'poleRaces':poleraces, 'wonChamp':wonchamp, 'currentStanding':currentStanding, 'teamsList': teams}

        return ret

    #ritorna numero gara data luogo info su vincitore e pole man ULTIMA GARA
    @app.route(PATH+"lastRace", methods=['GET'])
    def get_lastRace(): 
        url = "https://ergast.com/api/f1/current/last/results/1"
        response = urllib.request.urlopen(url)
        my_xml = response.read()
        my_dic = xmltodict.parse(my_xml)

        lastRace = my_dic['MRData']['RaceTable']['Race']
        raceRound = lastRace['@round']
        raceDate = lastRace['Date']
        raceCircuit = lastRace['Circuit']['CircuitName']+ ',' + lastRace['Circuit']['Location']['Locality'] + ',' + lastRace['Circuit']['Location']['Country']
        raceInfo = {'race': raceRound, 'raceDate':raceDate, 'raceCircuit': raceCircuit}

        numb = lastRace['ResultsList']['Result']['@number']
        driverId = lastRace['ResultsList']['Result']['Driver']['@driverId']
        driverName = lastRace['ResultsList']['Result']['Driver']['GivenName']
        driverSurname = lastRace['ResultsList']['Result']['Driver']['FamilyName']
        constructor = lastRace['ResultsList']['Result']['Constructor']['Name']
        winnerInfo = {'number': numb, 'driverId': driverId, 'driverName':driverName, 'driverSurname': driverSurname, 'constructor': constructor}

        url = "https://ergast.com/api/f1/current/last/qualifying/1"
        response = urllib.request.urlopen(url)
        my_xml = response.read()
        my_dic = xmltodict.parse(my_xml)

        lastRacePole = my_dic['MRData']['RaceTable']['Race']

        numb = lastRacePole['QualifyingList']['QualifyingResult']['@number']
        driverId = lastRacePole['QualifyingList']['QualifyingResult']['Driver']['@driverId']
        driverName = lastRacePole['QualifyingList']['QualifyingResult']['Driver']['GivenName']
        driverSurname = lastRacePole['QualifyingList']['QualifyingResult']['Driver']['FamilyName']
        constructor = lastRacePole['QualifyingList']['QualifyingResult']['Constructor']['Name']
        PoleManInfo = {'number': numb, 'driverId': driverId, 'driverName':driverName, 'driverSurname': driverSurname, 'constructor': constructor}
        
        ret ={'raceInfo': raceInfo, 'winnerInfo': winnerInfo, 'PoleManInfo': PoleManInfo}
        return json.dumps(ret), 200

     #ritorna numero gara data luogo info su vincitore e pole man ULTIMA GARA
   
    #ritorna numero gara data ora luogo PROSSIMA GARA
    @app.route(PATH+"NextRace", methods=['GET'])
    def get_nextRace():
        url = "https://ergast.com/api/f1/current/next"
        response = urllib.request.urlopen(url)
        my_xml = response.read()
        my_dic = xmltodict.parse(my_xml)

        lastRace = my_dic['MRData']['RaceTable']['Race']
        raceRound = lastRace['@round']
        raceDate = lastRace['Date']
        raceTime = lastRace['Time']
        raceCoordinate = lastRace['Circuit']['Location']['@lat'] +","+lastRace['Circuit']['Location']['@long']
        raceCircuit = lastRace['Circuit']['CircuitName']+ ',' + lastRace['Circuit']['Location']['Locality'] + ',' + lastRace['Circuit']['Location']['Country']
        raceInfo = {'race': raceRound, 'raceDate':raceDate, 'raceTime':raceTime + " Local time", 'raceCircuit': raceCircuit , 'raceLocation': raceCoordinate}

        ret ={'raceInfo': raceInfo}
        return json.dumps(ret), 200

    #ritorna la classifica piloti
    @app.route(PATH+"driverStandings", methods=['GET'])
    def get_driverStandings():
        ret = []
        url = "http://ergast.com/api/f1/current/driverStandings"
        response = urllib.request.urlopen(url)
        my_xml = response.read()

        my_dic = xmltodict.parse(my_xml)
        drivers_list = my_dic['MRData']['StandingsTable']['StandingsList']['DriverStanding']
        
        for t in drivers_list:
            driverId = t['Driver']['@driverId']
            driverName = t['Driver']['GivenName']
            driverSurname = t['Driver']['FamilyName']
            driverPermanentNumber = t['Driver']['PermanentNumber']
            driverPosition = t['@position']
            driverPoints = t['@points']
            driverTeam = t['Constructor']['Name']

            driver = {'driverId': driverId, 'driverName': driverName, 'driverSurname': driverSurname, 'driverTeam': driverTeam, 'driverPermanentNumber': driverPermanentNumber, 'driverPosition': driverPosition, 'driverPoints': driverPoints}
            ret.append(driver)
            
        return {"list": ret}, 200

    
    #DB Request

    # Create a new favorite resource SISTEMARE TOKEN
    @app.route(PATH+'createfavorite', methods=['POST'])
    def createFavorite():
        data = request.get_json()
        if data:
            print(data)
            try:
                # News verification
                if 'news' in data:
                    news = data['news']
                    #da sistemare con token
                    userid = data['userId']
                    #if 'id' in news and 'webImage' in news and 'webTitle' in news and 'webUrl' in news and 'webDesc' in news:
                    if 'webTitle' in news :
                        resourceCreated = dbFunctions.insertFavorite(DATABASE, userid, news)
                        if resourceCreated:
                            print('Created favorite: ('+userid+', '+news['id']+')')
                            return {'error': False}, 201
                        else:
                            # Duplicate
                            return {'error': True}, 401
                    else:
                        # Bad request
                        return {'error': True}, 402
                else:
                    # Bad request
                    return {'error': True}, 403
            except ValueError:
                # Invalid tokenID or forbidden insertion
                pass
        return {'error': True}, 404

    # Delete a favorite resource SISTEMA USER/TOKEN
    @app.route(PATH+'deletefavorite', methods=['DELETE'])
    def deleteFavorite():
        parser = reqparse.RequestParser()
        parser.add_argument('newsid', type=str, required=True)
        args = parser.parse_args()
        news = args['newsid']

        userid = "1"

        resourceDeleted = dbFunctions.deleteFavorite(DATABASE, userid, news)
        if resourceDeleted:
            print('Deleted favorite: ('+userid+', '+news+')')
            return {'error': False}, 200
        else:
            # Not found
            return {'error': True}, 404

        return {'error': True}, 403

    #Get Favorite of specific account
    @app.route(PATH+'favorites', methods=['GET'])
    def getFavorites():
        connection = dbFunctions.get_db_connection(DATABASE)
        userid = request.args.get('userId')
        favorites = connection.execute("SELECT webTitle, webImage, webDesc, webUrl FROM favorites JOIN news on content=id WHERE account='"+userid+"' ;").fetchall()
        connection.commit()
        connection.close()

        res = []
        for favorite in favorites:
            favorite = dict(favorite)
            res.append(favorite)

        return {'favorites': res}, 200

    #get all saved news
    @app.route(PATH+'news', methods=['GET'])
    def getNews():
        connection = dbFunctions.get_db_connection(DATABASE)
        favorites = connection.execute("SELECT webTitle, webImage, webDesc, webUrl FROM news;").fetchall()
        connection.commit()
        connection.close()
        
        res = []
        for favorite in favorites:
            favorite = dict(favorite)
            res.append(favorite)

        return {'news': res}, 200

    @app.route(PATH+'accounts', methods=['GET'])
    def getAccounts():
        connection = dbFunctions.get_db_connection(DATABASE)
        accounts = connection.execute('SELECT * FROM accounts;').fetchall()
        connection.commit()
        connection.close()

        res = []
        for account in accounts:
            account = dict(account)
            res.append(account)

        return {'accounts': res}, 200
    
    #check if exists account with given mail (completa)
    @app.route(PATH+'checkemail', methods=['GET'])
    def checkEmail():
        parser = reqparse.RequestParser()
        parser.add_argument('email', type=str, required=True)
        args = parser.parse_args()
        email = args['email']

        emailInDatabase = dbFunctions.checkIfEmailExists(DATABASE, email)
        if emailInDatabase == 1:
            return {'error': False}, 200
        else:
            return {'error': True}, 404
           
    # Check signed-in (NEW ACCOUNT)
    @app.route(PATH+'signin', methods=['POST'])
    def checkAccount():
        data = request.get_json()
        if data:
            print(data)
            try:
                userid = data['userid']
                email = data['email']
                if userid != "" and email != "":
                    dbFunctions.insertAccount(DATABASE, userid, "tokenID", email)
                    print('Signed-in account: '+userid+', '+email)
                    return {'error': False}, 201
            except ValueError:
                # Invalid token
                pass
        return {'error': True}, 403

    # Delete an account resource
    @app.route(PATH+'deleteaccount', methods=['DELETE'])
    def deleteAccount():
        parser = reqparse.RequestParser()
        parser.add_argument('userid', type=str, required=True)
        args = parser.parse_args()
        userid = args['userid']

        if userid != "":
            resourceDeleted = dbFunctions.deleteAccount(DATABASE, userid)
            if resourceDeleted:
                print('Deleted account: '+userid)
                return {'error': False}, 200
            else:
                # Not found
                return {'error': True}, 404

        return {'error': True}, 403


api.add_resource(F1Server, PATH)

if __name__=='__main__':
    print('Initializing database...')
    dbFunctions.init_db(DATABASE, SCHEMA)
    print('Starting web server...')

    app.run(host= HOST, port = '8000')

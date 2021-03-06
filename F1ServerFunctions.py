import urllib.request, json, xmltodict


def get_team_nation(teamName):
    url = "https://ergast.com/api/f1/constructors/"+teamName
    response = urllib.request.urlopen(url)
    my_xml = response.read()
    my_dic = xmltodict.parse(my_xml)
    nationality = my_dic['MRData']['ConstructorTable']['Constructor']['Nationality']
    return nationality

def get_team_current_drivers(teamName):
    url = "https://ergast.com/api/f1/current/constructors/"+ teamName +"/drivers"
    response = urllib.request.urlopen(url)
    my_xml = response.read()
    my_dic = xmltodict.parse(my_xml)
    currentDrivers = []
    if my_dic['MRData']['@total'] != "0":
        drivers_list = my_dic['MRData']['DriverTable']['Driver']
        for t in drivers_list:
            driverId = t['@driverId']
            driverName = t['GivenName']
            driverSurname = t['FamilyName']
            driverNumb = t['PermanentNumber']
            driver = {'permanentNumber':driverNumb, 'driverName': driverName, 'driverSurname': driverSurname, 'driverId': driverId}
            currentDrivers.append(driver)
    return currentDrivers

def get_team_standInfo(teamName):
    url = "https://ergast.com/api/f1/current/constructors/"+teamName+"/constructorStandings"
    response = urllib.request.urlopen(url)
    my_xml = response.read()
    my_dic = xmltodict.parse(my_xml)

    standInfo = {'rank' : '0'}
    standings = my_dic['MRData']['StandingsTable']
    if my_dic['MRData']['@total'] != '0' :
        rank = standings['StandingsList']['ConstructorStanding']['@position']
        point = standings['StandingsList']['ConstructorStanding']['@points']
        wins = standings['StandingsList']['ConstructorStanding']['@wins']
        standInfo = {'rank':rank, 'points': point, 'wins':wins}
    return standInfo

def get_team_results(teamName):
    url = "https://ergast.com/api/f1/constructors/"+teamName+"/results/1"
    response = urllib.request.urlopen(url)
    my_xml = response.read()
    my_dic = xmltodict.parse(my_xml)
    totalRaceWin = my_dic['MRData']['@total']
    return totalRaceWin

def get_team_standings(teamName):
    url = "https://ergast.com/api/f1/constructors/"+teamName+"/constructorStandings/1"
    response = urllib.request.urlopen(url)
    my_xml = response.read()
    my_dic = xmltodict.parse(my_xml)
    totalChamWin = my_dic['MRData']['@total']
    return totalChamWin

def get_driver_info(driverName):
    url = "https://ergast.com/api/f1/drivers/"+driverName
    response = urllib.request.urlopen(url)
    my_xml = response.read()
    my_dic = xmltodict.parse(my_xml)

    driver = my_dic['MRData']['DriverTable']['Driver']
    numb = driver['PermanentNumber']
    name = driver['GivenName']
    surname = driver['FamilyName']
    birth = driver['DateOfBirth']
    nationality = driver['Nationality']

    driverInfo = {'numb':numb, 'name':name, 'surname':surname, 'birth':birth, 'nationality': nationality}

    return driverInfo

def get_driver_wins(driverName):
    url = "https://ergast.com/api/f1/drivers/"+driverName+"/results/1"
    response = urllib.request.urlopen(url)
    my_xml = response.read()
    my_dic = xmltodict.parse(my_xml)
    wonraces = my_dic['MRData']['@total']

    return wonraces

def get_driver_pole(driverName):
    url = "https://ergast.com/api/f1/drivers/"+driverName+"/qualifying/1"
    response = urllib.request.urlopen(url)
    my_xml = response.read()
    my_dic = xmltodict.parse(my_xml)
    poleraces = my_dic['MRData']['@total']

    return poleraces

def get_driver_champions(driverName):
    url = "https://ergast.com/api/f1/drivers/"+driverName+"/driverStandings/1"
    response = urllib.request.urlopen(url)
    my_xml = response.read()
    my_dic = xmltodict.parse(my_xml)
    wonchamp = my_dic['MRData']['@total']

    return wonchamp

def get_driver_teams(driverName):
    url = "https://ergast.com/api/f1/drivers/"+driverName+"/constructors"
    response = urllib.request.urlopen(url)
    my_xml = response.read()
    my_dic = xmltodict.parse(my_xml)
    teams_list = my_dic['MRData']['ConstructorTable']['Constructor']
    teams = []
    if my_dic['MRData']['@total'] != '1':
        for t in teams_list:
            tid = t['@constructorId']
            name = t['Name']
            nationality = t['Nationality']
            team = {'id':tid, 'name': name, 'nationality': nationality }
            teams.append(team)
    else :
        tid = teams_list['@constructorId']
        name = teams_list['Name']
        nationality = teams_list['Nationality']
        team = {'id':tid, 'name': name, 'nationality': nationality }
        teams.append(team)
    return teams

def get_driver_currentStanding(driverName):
    url = "https://ergast.com/api/f1/current/drivers/"+driverName+"/driverStandings"
    response = urllib.request.urlopen(url)
    my_xml = response.read()
    my_dic = xmltodict.parse(my_xml)
    currentStanding = {'position': '0'}
    if my_dic['MRData']['@total'] != '0':
        standing = my_dic['MRData']['StandingsTable']['StandingsList']['DriverStanding']
        position = standing['@position']
        points =  standing['@points']
        wins = standing['@wins']
        team = standing['Constructor']['Name']
        currentStanding = {'position':position, 'points':points, 'wins':wins, 'team':team}
    return currentStanding

def get_circuit_results(circuitName):
    results = []
    for n in range(2017,2022):
        url = "https://ergast.com/api/f1/"+str(n)+"/circuits/"+circuitName+"/results/1"
        response = urllib.request.urlopen(url)
        my_xml = response.read()
        my_dic = xmltodict.parse(my_xml)
        if int(my_dic['MRData']['@total']) == 1:
            dri = my_dic['MRData']['RaceTable']['Race']['ResultsList']['Result']
            print("PILOTAAAA  ")
            print(dri['Driver'])
            print("\n")
            name = dri['Driver']['GivenName']
            surname = dri['Driver']['FamilyName']
            number = dri['Driver']['PermanentNumber']
            team = dri['Constructor']['Name']
            driver = {'name':name, 'surname':surname, 'number':number, 'team':team}
            results.append(driver)
        if int(my_dic['MRData']['@total']) > 1:
            dri = my_dic['MRData']['RaceTable']['Race'][0]['ResultsList']['Result']
            print("PILOTAAAA  ")
            print(dri['Driver'])
            print("\n")
            name = dri['Driver']['GivenName']
            surname = dri['Driver']['FamilyName']
            number = dri['Driver']['PermanentNumber']
            team = dri['Constructor']['Name']
            driver = {'name':name, 'surname':surname, 'number':number, 'team':team}
            results.append(driver)
    return results

def get_circuit_poles(circuitName):
    results = []
    for n in range(2017,2022):
        url = "https://ergast.com/api/f1/"+str(n)+"/circuits/"+circuitName+"/qualifying/1"
        response = urllib.request.urlopen(url)
        my_xml = response.read()
        my_dic = xmltodict.parse(my_xml)
        if int(my_dic['MRData']['@total']) == 1:
            dri = my_dic['MRData']['RaceTable']['Race']['QualifyingList']['QualifyingResult']
            name = dri['Driver']['GivenName']
            surname = dri['Driver']['FamilyName']
            number = dri['Driver']['PermanentNumber']
            team = dri['Constructor']['Name']
            time = dri['Q3']
            driver = {'name':name, 'surname':surname, 'number':number, 'team':team, 'time':time}
            results.append(driver)
        if int(my_dic['MRData']['@total']) > 1:
            dri = my_dic['MRData']['RaceTable']['Race'][0]['QualifyingList']['QualifyingResult']
            name = dri['Driver']['GivenName']
            surname = dri['Driver']['FamilyName']
            number = dri['Driver']['PermanentNumber']
            team = dri['Constructor']['Name']
            time = dri['Q3']
            driver = {'name':name, 'surname':surname, 'number':number, 'team':team, 'time':time}
            results.append(driver)
    return results

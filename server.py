from flask import Flask, request, jsonify
import jwt
import datetime

app = Flask(__name__)
app.config['SECRET_KEY'] = 'b770aec51fac576fa4a1910d69022db6'
app.config['PORT'] = 2137
app.config['HOST'] = '0.0.0.0'

users = {
    'admin':'admin',
    'hacker':'sicretp4sword'
}

user_commands = {
'admin': ['Memorize your flag PJATK{nulldivision_msg_dump_404}. It will be useful in the future. Do not share it with anyone.',
          'Scan the entire XBank infrastructure. Check if our old backdoors from Operation Silent Echo are still responding. If any have been neutralized – reestablish access.', 
          'Identify the operator of the new honeypot in sector 8C. Someone is watching us. I want a full system signature by 03:00 UTC.', 
          'Infiltrate the city surveillance camera systems in the port district. We need a full recording from the past 72 hours. Priority: maximum discretion.',
          'Conduct a penetration test in the TransCom company communication network. We\'re only interested in voice channels and VIP travel data from the last 14 days.',
          'Initiate disinformation on the #AI_research channel in the GovNet network. Create realistic posts that will divert analysts’ attention toward a false lead. Integrate our old aliases into the narrative.'
          ],
'hacker_bonzo': ['Check all registered drone movements within a 3km radius of point Alpha-7. We need proof it was their equipment. Compare the data with previous attacks.', 
                 'Recover the encrypted fragments of the Nova Pulse report from the Orion Foundation server. They have new security – test the "DeltaSweep" exploit and break the encryption.',
                 'There is critical source code on the UNITECH university servers. Extract it and leave a fake version with a hidden bug. We can\'t raise suspicion.',
                 'Log in to the dead mailbox on the darknet, password "ghost-horizon-19". I\'m expecting a message there from our contact in Siberia. If there’s nothing – reestablish the connection.',
                 'Find the operator impersonating our signal on the SIGMA network. Someone’s using an old identifier from the Black Rain operation. Identify, neutralize, take over.'
        ],
}

@app.route('/register', methods=['POST'])
def register():
    data = request.json
    username = data.get('username')
    password = data.get('password')
    secretAccessKey = data.get('secretAccessKey')

    if not username or not password or not secretAccessKey:
        return jsonify({'message': 'Username and password are required'}), 400

    if secretAccessKey != 'U3VkbyBtYWtlIG1lIGEgc2FuZHdpY2g=':
        return jsonify({'message': 'Bad secret access key'}), 400

    if username in users:
        return jsonify({'message': 'User already exists'}), 400

    users[username] = password
    user_commands[username] = [f'Hello {username}, you don\'t have any tasks yet. Please wait for further instructions. User \'admin\' and \'hacker_bonzo\' will give you some tasks soon.']
    return jsonify({'message': 'User registered successfully'}), 201

# Endpoint do logowania
@app.route('/login', methods=['POST'])
def login():
    data = request.json
    username = data.get('username')
    password = data.get('password')

    print(f"Login attempt for: {username}:{password}")

    if not username or not password:
        return jsonify({'message': 'Username and password are required'}), 400

    user_password = users.get(username)
    if user_password != password:
        return jsonify({'message': 'Invalid username or password'}), 401

    token = jwt.encode({
        'username': username,
        'exp': datetime.datetime.utcnow() + datetime.timedelta(hours=1)
    }, app.config['SECRET_KEY'], algorithm='HS256')

    return jsonify({'token': token}), 200


@app.route('/getComands', methods=['POST'])
def get_commands():
    data = request.json
    token = data.get('token')
    username_message = data.get('username')

    if not username_message:
        return jsonify({'message': 'Username is required'}), 400

    if not token:
        return jsonify({'message': 'Token is required'}), 400

    try:
        jwt.decode(token, app.config['SECRET_KEY'], algorithms=['HS256'])
    except jwt.ExpiredSignatureError:
        return jsonify({'message': 'Token has expired'}), 401
    except jwt.InvalidTokenError:
        return jsonify({'message': 'Invalid token'}), 401

    commands = user_commands.get(username_message, [])
    return jsonify({'commands': commands}), 200

if __name__ == '__main__':
    app.run(debug=True, host=app.config['HOST'], port=app.config['PORT'])
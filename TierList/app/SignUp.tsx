import React from "react";
import { useState } from 'react';
import { useRouter } from "expo-router";
import { Text, View, TouchableOpacity, TextInput, Alert } from "react-native";
import appStyles from "./styles/appStyles.js";

export default function SignUpScreen() {
  const router = useRouter();
  const [username, setUsername] = useState('');     // username value
  const [password, setPassword] = useState('');     // password value


  return (
    <View style={appStyles.container}>
      <Text style={appStyles.title}>Create Your Account!</Text>

      {/* prompt user for a username */}
      <TextInput
        style={appStyles.input}
        placeholder="Enter a username"
        value={username}
        onChangeText={setUsername}
      />

      {/* prompt user for a password */}
      <TextInput
        style={appStyles.input}
        placeholder="Enter a password"
        value={password}
        onChangeText={setPassword}
        secureTextEntry={true}
      />

      {/* send user entered credentials */}
      <TouchableOpacity style={appStyles.signUpButton}>
        <Text style={appStyles.buttonText}>Sign Up</Text>
      </TouchableOpacity>

      {/* send user to login page if user already has an account */}
      <Text style={appStyles.smallText}>Already a user?</Text>
      <TouchableOpacity style={[appStyles.button, appStyles.secondaryButton]}>
        <Text style={appStyles.buttonText}>Log In</Text>
      </TouchableOpacity>

    </View>
  );
}
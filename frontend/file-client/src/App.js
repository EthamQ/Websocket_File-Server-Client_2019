import React, { Component } from 'react';
import './App.css';


class App extends Component {
  render() {
    return (
      <div className="landing-image">
          <h1 className="app-text-center mt-3 ">Computer Networks - Web Sockets</h1>
          <div className="row">
          <div className="col-12">
          <input onChange={this.userInput} type="text" className="form-control app-input app-center-element-horiz mt-5" id="exampleInputEmail1" placeholder="Enter the file name you want to display"/>
          </div>
          <div className="col-12">
          <button className="btn btn-primary app-center-element-horiz mt-3" onClick={this.sendFileName}>Send filename</button>  
          </div> 
        {this.state.base64ImageData.length === 0 ? "" : 
        <div className="col-12 mt-5">
        <div className="row">
           <div className="col-6 d-flex justify-content-center"> 
            <p className="file-info-text">Showing: {this.state.fileName}</p>
            </div> 
            <div className="col-6"> 
            <img src={this.state.base64ImageData} alt="no img"/>
            </div> 
            </div> 
            </div>}
            {this.state.errorMessage.length == 0 ? "" :
            <div className="col-12 mt-5">
              <p className="app-text-center file-error-text">The following error occured on the server: {this.state.errorMessage}</p>
            </div>
            }
          </div>
      </div>
    );
  }

  websocket;
  url = "ws://127.0.0.1:8080/websocket";

  constructor(props) {
    super(props);
    this.state = {
      base64ImageData: "",
      fileName: "",
      userInput: "",
      errorMessage: ""
    };
    this.webSocket = new WebSocket(this.url, null);
    const thisClass = this;
    this.webSocket.onmessage = async function (event) {
      if(thisClass.isErrorResponse(event.data)) {
        thisClass.handleErrorResponse(event.data);
      } else {
        const fileData = await thisClass.blobToBase64(event.data);
        thisClass.setState({
          base64ImageData: fileData,
          errorMessage: ""
        });
      }
    }
  }

  sendFileName = () => {
    this.webSocket.send(this.state.userInput);
    this.setState({
      fileName: this.state.userInput
    });
  }

  userInput = (event) => {
    this.setState({
      userInput: event.target.value
    });
  }

  blobToBase64(blob) {
    return new Promise((resolve, reject) => {
      const fileReader = new FileReader();
      fileReader.readAsDataURL(blob); 
      fileReader.onload = () => resolve(fileReader.result);
      fileReader.onerror = err => reject(err);
  });
}

  isErrorResponse(serverResponse){
    return typeof serverResponse == 'string'
  };

  handleErrorResponse(errorMessage) {
    this.setState({
      base64ImageData: "",
      errorMessage: errorMessage
    });
  }
}


export default App;

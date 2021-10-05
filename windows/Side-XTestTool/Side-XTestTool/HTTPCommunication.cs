using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Text;
using System.Threading.Tasks;

namespace SideXTestTool
{
    public class HTTPCommunication
    {
        public class DataResp
        {
            public string code { get; set; }
            public string message { get; set; }
        }
        //string result = sendRequest(request, serverRequest).Result;
        public static async Task<DataResp> sendRequest(string reqUrl, string requestData, string methodType)
        {
            DataResp responseStr = null;
            using (HttpClient client = new HttpClient())
            {
               try
                {
                    HttpRequestMessage request = new HttpRequestMessage();
                    request.RequestUri = new Uri(Globals.BASE_URL + reqUrl);
                    request.Headers.Authorization = new AuthenticationHeaderValue("Basic", "dTRpYTp1NGlhVVNFUmFwaQ==");
                    request.Headers.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));
                    switch (methodType)
                    {
                        case "GET":
                            request.Method = HttpMethod.Get;
                            break;
                        case "PUT":
                             request.Method = HttpMethod.Put;
                            if (requestData != null)
                                request.Content = new StringContent(requestData, Encoding.UTF8, "application/json");
                            break;
                        case "POST":
                            request.Method = HttpMethod.Post;
                            if (requestData != null)
                                request.Content = new StringContent(requestData, Encoding.UTF8, "application/json");
                            break;
                        default:
                            break;
                    }

                    Console.WriteLine("****************Server Request: " + request.ToString());

                    if (request.Content != null)
                    {
                        Console.WriteLine("Request Data: " + request.Content.ReadAsStringAsync().Result);
                    }

                    HttpResponseMessage response = client.SendAsync(request).Result;

                    Console.WriteLine("****************Server Response: " + response.ToString());
                    if (response.Content != null)
                    {
                        Console.WriteLine("Response Data: " + response.Content.ReadAsStringAsync().Result);
                    }
                    responseStr = new DataResp();
                    if (response.IsSuccessStatusCode)
                    {
                        responseStr.message = response.Content.ReadAsStringAsync().Result;
                        responseStr.code = "0";
                        return responseStr;
                    }
                    else
                    {
                        try
                        {
                            DataResp jsonResp = JsonConvert.DeserializeObject<DataResp>(await response.Content.ReadAsStringAsync());
                            return jsonResp;
                               
                        }
                        catch (Exception ex)
                        {
                            responseStr.message = ex.Message;
                            responseStr.code = "99";
                            return responseStr;
                        }
                    }
                }
                catch (Exception ex)
                {
                    Console.WriteLine("sendRequest", "Exception: " + ex.Message);
                    responseStr.message = ex.Message;
                    responseStr.code = "99";
                    return responseStr;
                }
            }
        }


        public static async Task<string> SendRequest(string url, string requestData)
        {
            string returnValue = "";

            try
            {
                using (var client = new HttpClient())
                {
                    //generate url

                    client.BaseAddress = new Uri(url);
                    client.DefaultRequestHeaders.Accept.Clear();
                    client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));

                    // "basic "+ Convert.ToBase64String(byteArray)
                    AuthenticationHeaderValue ahv = new AuthenticationHeaderValue("Basic", "dTRpYTp1NGlhVVNFUmFwaQ==");
                    client.DefaultRequestHeaders.Authorization = ahv;

                    string requesturl = url;
                    HttpResponseMessage response = await client.GetAsync(requesturl);
                    response.EnsureSuccessStatusCode();
                    returnValue = ((HttpResponseMessage)response).Content.ReadAsStringAsync().Result;
                }
                return returnValue;
            }
            catch (Exception e)
            {
                throw (e);
            }
        }
    }
}

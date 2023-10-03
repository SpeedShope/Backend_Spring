import pandas as pd
import tensorflow as tf

# Load the product data from MySQL database
import mysql.connector

mydb = mysql.connector.connect(
     host='localhost:3306',
     user='root',
     password='',
     database='ACHREFtEST'
)

mycursor = mydb.cursor()

mycursor.execute("SELECT * FROM product")

product_data = pd.DataFrame(mycursor.fetchall(), columns=['name', 'price', 'number_of_rates', 'qte', 'accepted'])

# Split the data into training and testing sets
train_data = product_data.sample(frac=0.7, random_state=123)
test_data = product_data.drop(train_data.index)

# Define the feature columns
feature_columns = [
    tf.feature_column.numeric_column('price'),
    tf.feature_column.numeric_column('number_of_rates'),
    tf.feature_column.numeric_column('qte')
]

# Create the estimator
linear_est = tf.estimator.LinearClassifier(feature_columns=feature_columns)

# Define the input functions
train_input_fn = tf.compat.v1.estimator.inputs.pandas_input_fn(
    x=train_data.drop('accepted', axis=1),
    y=train_data['accepted'],
    num_epochs=None,
    shuffle=True
)

eval_input_fn = tf.compat.v1.estimator.inputs.pandas_input_fn(
    x=test_data.drop('accepted', axis=1),
    y=test_data['accepted'],
    num_epochs=1,
    shuffle=False
)

# Train the model
linear_est.train(train_input_fn)

# Evaluate the model on the testing data
result = linear_est.evaluate(eval_input_fn)
accuracy = result['accuracy']
print('Accuracy:', accuracy)

# Define a function to predict the acceptance of a new product
def predict_acceptance(product_data):
    new_product_df = pd.DataFrame([product_data])
    predict_input_fn = tf.compat.v1.estimator.inputs.pandas_input_fn(
        x=new_product_df,
        num_epochs=1,
        shuffle=False
    )
    pred = list(linear_est.predict(predict_input_fn))[0]['probabilities'][1]
    return pred
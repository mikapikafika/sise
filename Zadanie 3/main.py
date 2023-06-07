from sklearn.datasets import load_iris
from sklearn.model_selection import train_test_split
from sklearn.tree import DecisionTreeClassifier, plot_tree, export_graphviz
from sklearn.metrics import confusion_matrix, classification_report
import matplotlib.pyplot as plt
import seaborn as sns
import graphviz

# Wczytujemy zbiór danych irysów
iris = load_iris()

# Dzielimy zbiór irysów na część treningową (80%) i testową (20%)
X_train, X_test, y_train, y_test = train_test_split(iris.data, iris.target, test_size=0.2)

# Wyświetlamy diagram drzewa decyzyjnego
plt.figure()
# Inicjalizujemy klasyfikator drzewa decyzyjnego i trenujemy go na danych wejściowych (X) i docelowych (y).
# fit() dopasowuje model do danych treningowych, ucząc go,
# jak przewidywać etykiety klas na podstawie dostarczonych cech
clf = DecisionTreeClassifier().fit(X_train, y_train)
# plot_tree(clf, filled=True)
# plt.title("Drzewo decyzyjne trenowane na zbiorze irysów")
# plt.show()

# Macierz pomyłek w formie terminalowej...
predicted_labels = clf.predict(X_test)
confusion_matrix = confusion_matrix(y_test, predicted_labels)
print("Macierz pomyłek:")
print(confusion_matrix)

# ... i graficznej
labels = iris.target_names
sns.heatmap(confusion_matrix, annot=True, xticklabels=labels, yticklabels=labels)
plt.title("Macierz pomyłek")
plt.xlabel('Przewidywane etykiety')
plt.ylabel('Rzeczywiste etykiety')
plt.show()

# Wartości precision, recall, F-measure itd.
class_report = classification_report(y_test, predicted_labels, target_names=iris.target_names)
print("\nRaport klasyfikacji:")
print(class_report)
# support = ile próbek należy do danej klasy w zbiorze testowym

dot_data = export_graphviz(clf, out_file=None,
                           feature_names=iris.feature_names,
                           class_names=iris.target_names,
                           filled=True, rounded=True,
                           special_characters=True)
graph = graphviz.Source(dot_data)
graph.render("iris")
